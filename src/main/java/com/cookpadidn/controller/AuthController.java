package com.cookpadidn.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cookpadidn.dto.LoginRequest;
import com.cookpadidn.dto.SignupRequest;
import com.cookpadidn.dto.UserResponse;
import com.cookpadidn.exception.BadRequestException;
import com.cookpadidn.response.SuccessResponse;
import com.cookpadidn.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Tag(name = "Authentication", description = "Authentication Controller | Contains: login, signup, refresh token")
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    private static final String ACCESS_TOKEN = "access_token";

    private final AuthenticationManager authenticationManager;

    private final UserService userService;


    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<SuccessResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest servletRequest) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        User user = (User) authenticate.getPrincipal();

        UserResponse userResponse = userService.getUserByEmail(loginRequest.getEmail());

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(servletRequest.getRequestURI())
                .withClaim("roles", user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
        log.info("Info :  successfully generated access token user");

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3 * 86400 * 1000))
                .withIssuer(servletRequest.getRequestURL().toString())
                .sign(algorithm);
        log.info("Info :  successfully generated refresh token user");

        HashMap<String, Object> tokens = new HashMap<>();
        tokens.put(ACCESS_TOKEN, accessToken);
        tokens.put("refresh_token", refreshToken);
        tokens.put("data", userResponse);

        SuccessResponse response = new SuccessResponse(Boolean.TRUE, "Successfully Login", tokens);

        return ResponseEntity.ok()
                .header(ACCESS_TOKEN, accessToken)
                .body(response);
    }

    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<SuccessResponse> addUser(@Valid @RequestBody SignupRequest signupRequest, HttpServletRequest servletRequest){

        List<String> role = signupRequest.getRoles();

        com.cookpadidn.entity.User savedUser = userService.addUser(signupRequest, role);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(savedUser.getEmail());
        loginRequest.setPassword(signupRequest.getPassword());

        ResponseEntity<SuccessResponse> login = login(loginRequest, servletRequest);
        Object data = Objects.requireNonNull(login.getBody()).getData();

        SuccessResponse response = new SuccessResponse(
                Boolean.TRUE, "Successfully added user data with id : " + savedUser.getId(), data);
        log.info("successfully added user data with id : {}", savedUser.getId());

        return new ResponseEntity<>(response, CREATED);
    }

    @GetMapping(value = "/api/v1/auth/refresh")
    public ResponseEntity<SuccessResponse> refreshToken(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        String authHeader = servletRequest.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String email = decodedJWT.getSubject();
                UserResponse userByEmail = userService.getUserByEmail(email);

                String accessToken = JWT.create()
                        .withSubject(userByEmail.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(servletRequest.getRequestURI())
                        .withClaim("roles", userByEmail.getRole())
                        .sign(algorithm);

                HashMap<String, String> tokens = new HashMap<>();
                tokens.put(ACCESS_TOKEN, accessToken);
                tokens.put("refresh_token", refreshToken);
                servletResponse.setContentType(APPLICATION_JSON_VALUE);

                SuccessResponse response = new SuccessResponse(Boolean.TRUE, "Successfully created new access token",tokens);
                log.info("successfully created new access token");

                return ResponseEntity.ok()
                        .body(response);

            }catch (Exception exception){
                HashMap<String, String> errorMessage = new HashMap<>();
                errorMessage.put("error", exception.getMessage());
                throw new BadRequestException(errorMessage);
            }
        }else {
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "refresh token is missing or not with valid form");
            throw new BadRequestException(errorMessage);
        }
    }
}
