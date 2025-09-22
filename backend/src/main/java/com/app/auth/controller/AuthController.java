package com.app.auth.controller;

import com.app.auth.model.PasswordChangeRequest;
import com.app.auth.model.User;
import com.app.auth.service.UserService;
import com.app.auth.service.impl.UserServiceImpl;
import com.app.dto.CreateUserRequest;
import com.app.dto.LoginRequest;
import com.app.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> signUp(@RequestBody CreateUserRequest createUserRequest){
        log.info("CreateUserRequest request: {}", createUserRequest);
        return userService.signUp(createUserRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        String requestURL = request.getRequestURL().toString(); // Full URL without query params
        String queryString = request.getQueryString();          // Raw query string (can be null)
        String fullURL = requestURL + (queryString != null ? "?" + queryString : "");
        log.info("The full URL: {}", fullURL);
        return userService.login(loginRequest);
    }

    @PostMapping("/changePsw")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request){
        return userService.changePassword(request);
    }

    @GetMapping("/whoami")
    public Response<User> whoami() {
        return userService.whoami();
    }





}