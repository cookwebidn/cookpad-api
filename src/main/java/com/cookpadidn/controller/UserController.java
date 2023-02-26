package com.cookpadidn.controller;

import com.cookpadidn.dto.UserRequest;
import com.cookpadidn.dto.UserResponse;
import com.cookpadidn.entity.User;
import com.cookpadidn.response.SuccessResponse;
import com.cookpadidn.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/v1/user/get")
    public ResponseEntity<SuccessResponse> getUserByIdOrEmail(@RequestParam(required = false) String id, @RequestParam(required = false) String email) {

        final String message = "get user successfully";

        if (id != null) {
            UserResponse userResponse = userService.getUserById(id);
            SuccessResponse response = SuccessResponse.builder()
                    .success(Boolean.TRUE)
                    .message(message)
                    .data(userResponse)
                    .build();

            return new ResponseEntity<>(response, OK);

        } else if (email != null) {
            UserResponse userResponse = userService.getUserByEmail(email);
            SuccessResponse response = SuccessResponse.builder()
                    .success(Boolean.TRUE)
                    .message(message)
                    .data(userResponse)
                    .build();

            return new ResponseEntity<>(response, OK);
        }

        SuccessResponse response = SuccessResponse.builder()
                .success(Boolean.FALSE)
                .message("Insert the id or email")
                .build();

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @GetMapping("api/v1/user/get/all")
    public ResponseEntity<SuccessResponse> getAllUsers() {

        List<UserResponse> users = userService.getAllUsers();

        if (!users.isEmpty()) {

            SuccessResponse response = SuccessResponse.builder()
                    .success(Boolean.TRUE)
                    .message("get user successfully")
                    .data(users)
                    .build();

            return new ResponseEntity<>(response, OK);
        }

        return new ResponseEntity<>(new SuccessResponse(Boolean.TRUE, "User data is empty", users), OK);
    }

    @PutMapping("api/v1/user/update")
    public ResponseEntity<SuccessResponse> updateUser(@RequestParam @NotBlank String id, @Valid @RequestBody UserRequest userRequest) {

        User user = userService.updateUser(userRequest, id);

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        SuccessResponse response = SuccessResponse.builder()
                .success(Boolean.TRUE)
                .message("successfully updated user with id : " + user.getId())
                .data(userResponse)
                .build();

        return new ResponseEntity<>(response, OK);
    }

    @DeleteMapping("api/v1/user/delete")
    public ResponseEntity<SuccessResponse> deleteUser(@RequestParam String id) {

        User user = userService.deleteUser(id);
        SuccessResponse response = SuccessResponse.builder()
                .success(Boolean.TRUE)
                .message("successfully deleted user with id : " + user.getId())
                .build();

        return new ResponseEntity<>(response, OK);
    }
}
