package com.app.auth.service.impl;

import com.app.auth.exception.UserNotFoundException;
import com.app.auth.model.PasswordChangeRequest;
import com.app.auth.model.User;
import com.app.auth.model.enums.UserRole;
import com.app.auth.repository.UserRepository;
import com.app.auth.service.UserService;
import com.app.dto.CreateUserRequest;
import com.app.dto.LoginRequest;
import com.app.dto.Response;
import com.app.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementation of {@link UserService} for managing user authentication
 * and user-related operations.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private final String USER_NOT_FOUND_MSG = "User not found";

    /**
     * Constructs a UserServiceImpl with required dependencies.
     *
     * @param userRepository  Repository for User persistence
     * @param passwordEncoder PasswordEncoder for hashing passwords
     * @param jwtUtils        Utility class for generating and validating JWT tokens
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    // -------------------------------------------------------------------------
    // 🔹 USER REGISTRATION
    // -------------------------------------------------------------------------

    /**
     * Registers a new user in the system.
     *
     * @param createUserRequest DTO containing registration details
     * @return ResponseEntity with success or error message
     */
    @Override
    public ResponseEntity<String> signUp(CreateUserRequest createUserRequest) {
        log.info("Inside signUp()");
        Optional<User> existingUser = userRepository.findByEmail(createUserRequest.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username already taken");
        }

        UserRole assignedUserRole = (createUserRequest.getRole() == null) ?
                UserRole.USER : UserRole.valueOf(createUserRequest.getRole());

        User savedUser = userRepository.save(
                User.builder()
                        .name(createUserRequest.getName())
                        .email(createUserRequest.getEmail())
                        .password(passwordEncoder.encode(createUserRequest.getPassword()))
                        .role(assignedUserRole.name())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isEnabled(true)
                        .build()
        );

        log.info("The new user: {}", savedUser);
        return ResponseEntity.ok("User registered successfully");
    }

    // -------------------------------------------------------------------------
    // 🔹 USER LOGIN
    // -------------------------------------------------------------------------

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginRequest DTO containing email and password
     * @return ResponseEntity containing JWT token and user info or error
     */
    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        log.info("Inside login()");

        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder().message(USER_NOT_FOUND_MSG).build());
        }

        User user = optionalUser.get();

        if (!user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().message("User is disabled").build());
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Invalid Password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().message("Invalid password").build());
        }

        String token = jwtUtils.generateToken(user.getEmail());
        log.info("Returning the login info");

        return ResponseEntity.ok(
                Response.<User>builder()
                        .message(token)
                        .data(user)
                        .build()
        );
    }

    // -------------------------------------------------------------------------
    // 🔹 PASSWORD MANAGEMENT
    // -------------------------------------------------------------------------

    /**
     * Changes the password of an existing user.
     *
     * @param request DTO containing old and new passwords
     * @return ResponseEntity with updated user or error message
     */
    @Override
    public ResponseEntity<?> changePassword(PasswordChangeRequest request) {
        log.info("Changing user's password");

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder().message(USER_NOT_FOUND_MSG).build());
        }

        User user = optionalUser.get();
        if (!user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().message("User is disabled").build());
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            log.info("Invalid Password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().message("Invalid password").build());
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return ResponseEntity.ok(userRepository.save(user));
    }

    // -------------------------------------------------------------------------
    // 🔹 CURRENT USER INFO
    // -------------------------------------------------------------------------

    /**
     * Retrieves the currently authenticated user.
     *
     * @return User object representing the logged-in user
     */
    @Override
    public User getCurrentLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG));
    }


    // -------------------------------------------------------------------------
    // 🔹 USER MANAGEMENT
    // -------------------------------------------------------------------------

    /**
     * Disables a user account by email.
     *
     * @param email Email of the user to disable
     * @return Success message
     */
    @Override
    public String disableUser(String email) {
        User userToDisable = getUserByEmail(email);
        userToDisable.setEnabled(false);
        userRepository.save(userToDisable);
        return "Disabled successfully the user with the email: " + email;
    }

    // -------------------------------------------------------------------------
    // 🔹 INTERNAL HELPERS
    // -------------------------------------------------------------------------

    /**
     * Retrieves a user by email or throws UserNotFoundException.
     *
     * @param email Email of the user
     * @return User object
     */
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with the email: " + email));
    }
}
