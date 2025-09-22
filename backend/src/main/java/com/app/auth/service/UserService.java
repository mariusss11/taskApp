package com.app.auth.service;

import com.app.auth.model.PasswordChangeRequest;
import com.app.auth.model.User;
import com.app.dto.CreateUserRequest;
import com.app.dto.LoginRequest;
import com.app.dto.Response;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<String> signUp(CreateUserRequest createUserRequest);
    ResponseEntity<?> login(LoginRequest userRequest);

    User getCurrentLoggedInUser();

    Response<User> whoami();

    String disableUser(String email);

    ResponseEntity<?> changePassword(PasswordChangeRequest request);
}