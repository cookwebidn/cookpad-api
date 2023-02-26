package com.cookpadidn.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cookpadidn.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals("/api/v1/auth/login")){
            filterChain.doFilter(request,servletResponse);
        }else {
            String authHeader = request.getHeader(AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")){
                try {
                    String token = authHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,servletResponse);

                }catch (Exception exception) {
                    HashMap<String, String> message = new HashMap<>();
                    message.put("error", exception.getMessage());
                    ErrorResponse errorResponse = ErrorResponse.builder().
                            timestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                            .status(FORBIDDEN.value())
                            .error(FORBIDDEN.name())
                            .message(message)
                            .path(request.getRequestURI())
                            .request(request.getMethod())
                            .description("check for valid authorities")
                            .build();
                    servletResponse.setContentType(APPLICATION_JSON_VALUE);
                    logger.info(exception.getMessage());
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    servletResponse.setStatus(FORBIDDEN.value());
                    objectMapper.writeValue(servletResponse.getOutputStream(), errorResponse);
                }
            }else {
                filterChain.doFilter(request, servletResponse);
            }
        }
    }
}
