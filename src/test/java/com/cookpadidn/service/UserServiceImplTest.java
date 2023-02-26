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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void registerUserSuccess() {

        Role userRole = new Role();
        userRole.setId(UUID.randomUUID());
        userRole.setName("ROLE_USER");

        Role adminRole = new Role();
        userRole.setId(UUID.randomUUID());
        userRole.setName("ROLE_ADMIN");

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("John Doe");
        signupRequest.setEmail("johndoe@gmail.com");
        signupRequest.setPassword("password");
        List<String> roleUser = List.of("ROLE_USER");
        List<String> roleUserAdmin = List.of("ROLE_ADMIN", "ROLE_USER");

        User user = User.builder()
                        .name(signupRequest.getName())
                        .email(signupRequest.getEmail())
                        .password(signupRequest.getPassword())
                        .build();

        when(roleRepository.findRoleByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(roleRepository.findRoleByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));

        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.addUser(signupRequest, roleUser);
        userService.addUser(signupRequest, roleUserAdmin);
        userService.addUser(signupRequest, null);

        verify(userRepository, times(3)).findUserByEmail(signupRequest.getEmail());
        verify(roleRepository, times(4)).findRoleByName(anyString());
    }

    @Test
    void registerUserFailedEmailHasTaken() {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("John Doe");
        signupRequest.setEmail("johndoe@gmail.com");
        signupRequest.setPassword("password");
        List<String> roleUser = List.of("ROLE_USER");

        User user = User.builder()
                    .name(signupRequest.getName())
                    .email(signupRequest.getEmail())
                    .password(signupRequest.getPassword())
                    .build();

        when(userRepository.findUserByEmail("johndoe@gmail.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.addUser(signupRequest, roleUser))
                .isInstanceOf(BadRequestException.class);

        verify(userRepository, times(1)).findUserByEmail("johndoe@gmail.com");
    }

    @Test
    void registerUserFailedRoleNotFound() {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("John Doe");
        signupRequest.setEmail("johndoe@gmail.com");
        signupRequest.setPassword("password");
        List<String> roleUser = List.of("ROLE_USER");
        List<String> roleUserAdmin = List.of("ROLE_ADMIN", "ROLE_USER");

        when(userRepository.findUserByEmail("johndoe@gmail.com")).thenReturn(Optional.empty());

        when(roleRepository.findRoleByName("ROLE_USER")).thenReturn(Optional.empty());

        when(roleRepository.findRoleByName("ROLE_ADMIN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.addUser(signupRequest, roleUser))
                .isInstanceOf(ResourceNotFoundException.class);

        assertThatThrownBy(() -> userService.addUser(signupRequest, roleUserAdmin))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository, times(2)).findUserByEmail("johndoe@gmail.com");
        verify(roleRepository, times(2)).findRoleByName(anyString());
    }

    @Test
    void updateUserSuccess() {

        String encodedPassword = new BCryptPasswordEncoder().encode("password123");

        User existingUser = new User();
        existingUser.setId(UUID.randomUUID());
        existingUser.setName("John");
        existingUser.setEmail("john@gmail.com");
        existingUser.setPassword(encodedPassword);

        when(userRepository.findUserById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        UserRequest userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setEmail("johndoe@gmail.com");
        userRequest.setPassword("password123");

        User updatedUser = userService.updateUser(userRequest, existingUser.getId().toString());

        verify(userRepository).save(updatedUser);

        assertEquals(userRequest.getName(), updatedUser.getName());
        assertEquals(userRequest.getEmail(), updatedUser.getEmail());
        assertTrue(new BCryptPasswordEncoder().matches(userRequest.getPassword(), updatedUser.getPassword()));
    }

    @Test
    void updateUserFailedIdNotFound() {

        UUID id = UUID.randomUUID();

        UserRequest userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setEmail("johndoe@gmail.com");
        userRequest.setPassword("password123");

        User user = new User();
        user.setId(id);
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());

        when(userRepository.findUserById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(userRequest, id.toString()))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository, times(1)).findUserById(id);
    }

    @Test
    void updateUserFailedEmailHasTaken() {

        UUID id = UUID.randomUUID();

        UserRequest userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setEmail("johndoe@gmail.com");
        userRequest.setPassword("password123");

        User user = new User();
        user.setId(id);
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());

        when(userRepository.findUserById(any())).thenReturn(Optional.of(user));

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.updateUser(userRequest, id.toString()))
                .isInstanceOf(BadRequestException.class);

        verify(userRepository, times(1)).findUserById(id);
        verify(userRepository, times(1)).findUserByEmail(userRequest.getEmail());
    }

    @Test
    void getUserByIdSuccess() {

        UUID id = UUID.randomUUID();

        User user = new User();
        user.setId(id);
        user.setName("John");
        user.setEmail("john@gmail.com");
        user.setPassword("password");

        when(userRepository.findUserById(any())).thenReturn(Optional.of(user));

        UserResponse retrievedUser = userService.getUserById(id.toString());

        assertEquals(user.getEmail(), retrievedUser.getEmail());

        verify(userRepository, times(1)).findUserById(id);
    }

    @Test
    void getUserByIdFailedNotFound() {

        UUID userId = UUID.randomUUID();

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId.toString()))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository, times(1)).findUserById(userId);
    }

    @Test
    void getUserByEmailSuccess() {

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("John");
        user.setEmail("john@gmail.com");
        user.setPassword("password");

        when(userRepository.findUserByEmail("john@gmail.com")).thenReturn(Optional.of(user));

        UserResponse retrievedUser = userService.getUserByEmail("john@gmail.com");

        assertEquals(user.getEmail(), retrievedUser.getEmail());

        verify(userRepository, times(1)).findUserByEmail("john@gmail.com");
    }

    @Test
    void getUserByEmailFailedNotFound() {

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByEmail("email"))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository, times(1)).findUserByEmail(anyString());
    }

    @Test
    void getAllUsersSuccess() {

        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setName("John");
        user1.setEmail("john@email.com");
        user1.setPassword("john123456");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setName("Doe");
        user2.setEmail("doe@email.com");
        user2.setPassword("doe123456");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        userService.getAllUsers();

        verify(userRepository).findAll();
    }

    @Test
    void getAllUsersFailedNotFound() {

        when(userRepository.findAll()).thenThrow(new RuntimeException("Failed to get user list"));

        Throwable exception = catchThrowable(() -> userService.getAllUsers());

        verify(userRepository, times(1)).findAll();

        assertThat(exception).isInstanceOf(RuntimeException.class).hasMessage("Failed to get user list");
    }

    @Test
    void deleteUserSuccess() {

        UUID id = UUID.randomUUID();

        User user = new User();
        user.setId(id);
        user.setName("John");
        user.setEmail("john@gmail.com");
        user.setPassword("password");

        when(userRepository.findUserById(any())).thenReturn(Optional.of(user));

        User deletedUser = userService.deleteUser(id.toString());

        assertEquals(user.getId(), deletedUser.getId());

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUserFailedNotFound() {

        when(userRepository.findUserById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(UUID.randomUUID().toString()))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository, times(1)).findUserById(any());
    }

    @Test
    void loadUserByEmailSuccess() {

        UUID id = UUID.randomUUID();

        User user = new User();
        user.setId(id);
        user.setName("John");
        user.setEmail("john@gmail.com");
        user.setPassword("password");

        when(userRepository.findUserByEmail("john@gmail.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("john@gmail.com");

        Assertions.assertEquals(user.getEmail(), userDetails.getUsername());
        verify(userRepository, times(1)).findUserByEmail("john@gmail.com");
    }
}
