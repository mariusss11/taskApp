package com.app.auth.service.impl;

import com.app.auth.exception.UserNotFoundException;
import com.app.auth.model.PasswordChangeRequest;
import com.app.auth.model.User;
import com.app.auth.model.enums.UserRole;
import com.app.auth.repository.UserRepository;
import com.app.dto.CreateUserRequest;
import com.app.dto.LoginRequest;
import com.app.dto.Response;
import com.app.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtils jwtUtils;
    @Mock private Authentication authentication;
    @Mock private SecurityContext securityContext;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = User.builder()
                .id(1)
                .name("John Doe")
                .email("john@example.com")
                .password("encodedPass")
                .role(UserRole.USER.name())
                .isEnabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        SecurityContextHolder.clearContext();
    }

    // -------------------------------------------------------------------------
    // 🔹 signUp()
    // -------------------------------------------------------------------------
    @Test
    void signUp_ShouldRegisterNewUser_WhenEmailNotTaken() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("John");
        request.setEmail("john@example.com");
        request.setPassword("1234");
        request.setRole("USER");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("encoded123");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        ResponseEntity<String> response = userService.signUp(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signUp_ShouldReturnError_WhenEmailAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("john@example.com");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));

        ResponseEntity<String> response = userService.signUp(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username already taken", response.getBody());
        verify(userRepository, never()).save(any(User.class));
    }

    // -------------------------------------------------------------------------
    // 🔹 login()
    // -------------------------------------------------------------------------
    @Test
    void login_ShouldReturnToken_WhenCredentialsValid() {
        LoginRequest request = new LoginRequest("john@example.com", "1234");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("1234", "encodedPass")).thenReturn(true);
        when(jwtUtils.generateToken("john@example.com")).thenReturn("jwt-token");

        ResponseEntity<?> response = userService.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response<?> body = (Response<?>) response.getBody();
        assertEquals("jwt-token", body.getMessage());
        assertEquals(mockUser, body.getData());
    }

    @Test
    void login_ShouldReturnNotFound_WhenUserMissing() {
        LoginRequest request = new LoginRequest("missing@example.com", "1234");
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userService.login(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Response<?> body = (Response<?>) response.getBody();
        assertEquals("User not found", body.getMessage());
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenPasswordInvalid() {
        LoginRequest request = new LoginRequest("john@example.com", "wrong");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrong", "encodedPass")).thenReturn(false);

        ResponseEntity<?> response = userService.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Response<?> body = (Response<?>) response.getBody();
        assertEquals("Invalid password", body.getMessage());
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenUserDisabled() {
        mockUser.setEnabled(false);
        LoginRequest request = new LoginRequest("john@example.com", "1234");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));

        ResponseEntity<?> response = userService.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Response<?> body = (Response<?>) response.getBody();
        assertEquals("User is disabled", body.getMessage());
    }

    // -------------------------------------------------------------------------
    // 🔹 changePassword()
    // -------------------------------------------------------------------------
    @Test
    void changePassword_ShouldUpdatePassword_WhenValidRequest() {
        PasswordChangeRequest req = new PasswordChangeRequest("john@example.com", "1234", "new123");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("1234", "encodedPass")).thenReturn(true);
        when(passwordEncoder.encode("new123")).thenReturn("encodedNew");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        ResponseEntity<?> response = userService.changePassword(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository).save(mockUser);
    }

    @Test
    void changePassword_ShouldReturnUnauthorized_WhenOldPasswordInvalid() {
        PasswordChangeRequest req = new PasswordChangeRequest("john@example.com", "wrong", "new");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrong", "encodedPass")).thenReturn(false);

        ResponseEntity<?> response = userService.changePassword(req);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Response<?> body = (Response<?>) response.getBody();
        assertEquals("Invalid password", body.getMessage());
    }

    @Test
    void changePassword_ShouldReturnNotFound_WhenUserMissing() {
        PasswordChangeRequest req = new PasswordChangeRequest("missing@example.com", "1234", "new");
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userService.changePassword(req);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // -------------------------------------------------------------------------
    // 🔹 getCurrentLoggedInUser()
    // -------------------------------------------------------------------------
    @Test
    void getCurrentLoggedInUser_ShouldReturnUser_WhenAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("john@example.com");
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));

        User result = userService.getCurrentLoggedInUser();

        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void getCurrentLoggedInUser_ShouldThrow_WhenUserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("missing@example.com");
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getCurrentLoggedInUser());
    }

    // -------------------------------------------------------------------------
    // 🔹 disableUser()
    // -------------------------------------------------------------------------
    @Test
    void disableUser_ShouldDisableUserAndSave() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));

        String message = userService.disableUser("john@example.com");

        assertFalse(mockUser.isEnabled());
        assertTrue(message.contains("Disabled successfully"));
        verify(userRepository).save(mockUser);
    }

    @Test
    void disableUser_ShouldThrow_WhenUserMissing() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.disableUser("missing@example.com"));
    }
}
