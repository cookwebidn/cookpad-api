package com.cookpadidn.service;

import com.cookpadidn.dto.SignupRequest;
import com.cookpadidn.dto.UserRequest;
import com.cookpadidn.dto.UserResponse;
import com.cookpadidn.entity.Role;
import com.cookpadidn.entity.User;
import com.cookpadidn.exception.BadRequestException;
import com.cookpadidn.exception.ResourceNotFoundException;
import com.cookpadidn.repository.RoleRepository;
import com.cookpadidn.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private final Logger log =  LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String ERROR  = "ERROR ";
    private static final String EMAIL_LOG = "email has taken";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String INFO = "INFO : {} has assigned to ";

    @Override
    public User addUser(SignupRequest signupRequest, List<String> userRole) {

        if (userRepository.findUserByEmail(signupRequest.getEmail()).isPresent()){
            log.warn(EMAIL_LOG);
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put(ERROR, EMAIL_LOG);
            throw new BadRequestException(errorMessage);
        }

        List<Role> roles = new LinkedList<>();
        User user = User.builder()
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .build();

        if (userRole == null){
            Role role = roleRepository.findRoleByName(ROLE_USER).orElseThrow();
            log.info(INFO + ROLE_USER, user.getEmail());
            roles.add(role);
        }else{
            userRole.forEach(role -> {
                if (ROLE_ADMIN.equals(role)) {
                    Role roleAdmin = roleRepository.findRoleByName(ROLE_ADMIN).orElseThrow(
                            () -> {
                                log.error(ERROR + ROLE_ADMIN + " NOT FOUND");
                                throw new ResourceNotFoundException("Role", "role", ROLE_ADMIN);
                            });
                    roles.add(roleAdmin);
                    log.info(INFO + ROLE_ADMIN, user.getEmail());
                } else {
                    Role roleUser = roleRepository.findRoleByName(ROLE_USER).orElseThrow(
                            () -> {
                                log.error(ERROR + ROLE_USER + " NOT FOUND");
                                throw new ResourceNotFoundException("Role", "role", ROLE_USER);
                            });
                    roles.add(roleUser);
                    log.info(INFO + ROLE_USER, roleUser.getId());
                }
            });
        }
        String encryptedPassword = bCryptPasswordEncoder.encode(signupRequest.getPassword());
        log.info("Info : Password has been encrypted" );

        user.setPassword(encryptedPassword);
        user.setRoles(roles);
        log.info("successfully persist user to database");

        return userRepository.save(user);
    }

    @Override
    public UserResponse getUserById(String id) {

        UUID uuid = UUID.fromString(id);

        User user = userRepository.findUserById(uuid).orElseThrow(() -> {
            ResourceNotFoundException ex = new ResourceNotFoundException("user", "id", id);
            ex.setApiResponse();
            log.error(ex.getMessageMap().get(ERROR));
            throw ex;
        });

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRoles().stream().map(Role::getName).toList())
                .build();

        log.info("successfully retrieved user by id : {}", id);

        return userResponse;
    }

    @Override
    public UserResponse getUserByEmail(String email) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> {
                    ResourceNotFoundException ex = new ResourceNotFoundException("user", "email", email);
                    ex.setApiResponse();
                    log.error(ex.getMessageMap().get(ERROR));
                    throw ex;
                });

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRoles().stream().map(Role::getName).toList())
                .build();
        log.info("successfully retrieved user by email : {}", email);

        return userResponse;
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(user -> UserResponse.builder()
                            .id(user.getId().toString())
                            .name(user.getName())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .role(user.getRoles().stream().map(Role::getName).toList())
                            .build()
                ).toList();

        if (!users.isEmpty()) {
            log.info("Successfully retrieved all users data");
            return userResponses;
        }

        log.warn("No users found");
        return userResponses;
    }

    @Override
    public User updateUser(UserRequest userRequest, String id) {

        UUID uuid = UUID.fromString(id);

        User user = userRepository.findUserById(uuid).orElseThrow(() -> {
            ResourceNotFoundException ex = new ResourceNotFoundException("user", "id", id);
            ex.setApiResponse();
            log.error(ex.getMessageMap().get(ERROR));
            throw ex;
        });
        log.info("User found with id {}", id);

        boolean isEmailPresent = userRepository.findUserByEmail(userRequest.getEmail()).isPresent();

        if (isEmailPresent){
            log.warn(EMAIL_LOG);
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put(ERROR, EMAIL_LOG);
            throw new BadRequestException(errorMessage);
        }else {
            if (bCryptPasswordEncoder.matches(userRequest.getPassword(), user.getPassword())){
                String encryptedPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());
                user.setName(userRequest.getName());
                user.setEmail(userRequest.getEmail());
                user.setPassword(encryptedPassword);

                userRepository.save(user);
                log.info("successfully updated user with id {}", id);
            }else {
                HashMap<String, String> errorMessage = new HashMap<>();
                errorMessage.put(ERROR, "Password doesn't match");
                throw new BadRequestException(errorMessage);
            }
        }

        return user;
    }

    @Override
    public User deleteUser(String id) {

        UUID uuid = UUID.fromString(id);

        User user = userRepository.findUserById(uuid)
                .orElseThrow(() -> {
                    ResourceNotFoundException ex = new ResourceNotFoundException("user", "id", uuid.toString());
                    ex.setApiResponse();
                    log.error(ex.getMessageMap().get(ERROR));
                    throw ex;
                });
        userRepository.delete(user);
        log.info("successfully deleted user with id : {}", id);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User users = userRepository.findUserByEmail(email)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Email Not Found");
                });
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        users.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(users.getEmail(), users.getPassword(), authorities);
    }

}
