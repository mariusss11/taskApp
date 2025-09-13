package com.app.auth.service.impl;

import com.app.auth.exception.UserNotFoundException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ResponseEntity<String> signUp(CreateUserRequest createUserRequest) {
        log.info("Inside signUp()");
        Optional<User> existingUser = userRepository.findByEmail(createUserRequest.getEmail());

        if (existingUser.isPresent())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username already taken");

        UserRole assignedUserRole = (createUserRequest.getRole() == null) ?
                UserRole.USER : UserRole.valueOf(createUserRequest.getRole());

        //save the user
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

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        log.info("Inside login()");

        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .message("User not found").build());

        User user = optionalUser.get();

        if (!user.isEnabled())
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().message("User is disabled").build());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Invalid Password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
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

    @Override
    public User getCurrentLoggedInUser() {
        String  username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
                .orElseThrow(()-> new UserNotFoundException("User not found"));
    }

    @Override
    public String disableUser(String email) {
        User userToDisable = getUserByEmail(email);
        userToDisable.setEnabled(false);
        userRepository.save(userToDisable);
        return "Disabled successfully the user with the email: " + email;
    }

    @Override
    public Response<User> whoami() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Auth: {}",  auth);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUserByEmail(email);

        return Response.<User>builder()
                .statusCode(HttpStatus.OK.value())
                .data(user)
                .message("Successfully finished the whoami method")
                .build();
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with the email: " + email));
    }

//    private ResponseEntity<String> rollbackUser(User savedUser) {
//        log.warn("Error creating the client");
//        log.info("Rollback the saved user");
//        userRepository.deleteById(savedUser.getUserId());
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body("Error occurred creating the user");
//    }

}
