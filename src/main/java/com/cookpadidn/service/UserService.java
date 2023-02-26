package com.cookpadidn.service;

import com.cookpadidn.dto.SignupRequest;
import com.cookpadidn.dto.UserRequest;
import com.cookpadidn.dto.UserResponse;
import com.cookpadidn.entity.User;

import java.util.List;

public interface UserService {

    User addUser(SignupRequest signupRequest, List<String> role);
    UserResponse getUserById(String id);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getAllUsers();
    User updateUser(UserRequest userRequest, String id);
    User deleteUser(String id);
}
