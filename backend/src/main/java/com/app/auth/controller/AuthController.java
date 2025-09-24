package com.app.auth.controller;

import com.app.auth.model.PasswordChangeRequest;
import com.app.auth.model.User;
import com.app.auth.service.UserService;
import com.app.dto.CreateUserRequest;
import com.app.dto.LoginRequest;
import com.app.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication-related operations.
 * <p>
 * Handles user registration, login, password changes, and retrieving
 * information about the currently authenticated user.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    /**
     * Constructs an AuthController with the required UserService.
     *
     * @param userService Service layer for user-related operations
     */
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // 🔹 REGISTER USER
    // -------------------------------------------------------------------------

    /**
     * Registers a new user.
     *
     * @param createUserRequest DTO containing user registration details
     * @return ResponseEntity with success or error message
     */
    @PostMapping("/register")
    public ResponseEntity<String> signUp(@RequestBody CreateUserRequest createUserRequest) {
        log.info("CreateUserRequest request: {}", createUserRequest);
        return userService.signUp(createUserRequest);
    }

    // -------------------------------------------------------------------------
    // 🔹 LOGIN
    // -------------------------------------------------------------------------

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginRequest DTO containing email and password
     * @param request      HttpServletRequest for logging full request URL
     * @return ResponseEntity containing the JWT token and user info
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String fullURL = requestURL + (queryString != null ? "?" + queryString : "");
        log.info("The full URL: {}", fullURL);
        return userService.login(loginRequest);
    }

    // -------------------------------------------------------------------------
    // 🔹 CHANGE PASSWORD
    // -------------------------------------------------------------------------

    /**
     * Changes the password for an existing user.
     *
     * @param request DTO containing old and new passwords
     * @return ResponseEntity with the updated user or error message
     */
    @PostMapping("/changePsw")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        return userService.changePassword(request);
    }

    // -------------------------------------------------------------------------
    // 🔹 CURRENT USER INFO
    // -------------------------------------------------------------------------

    /**
     * Retrieves information about the currently authenticated user.
     *
     * @return Response containing the current User
     */
    @GetMapping("/whoami")
    public Response<User> whoami() {
        return userService.whoami();
    }
}
