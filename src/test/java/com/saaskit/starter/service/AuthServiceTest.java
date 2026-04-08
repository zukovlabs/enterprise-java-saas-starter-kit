package com.saaskit.starter.service;

import com.saaskit.starter.dto.AuthResponse;
import com.saaskit.starter.dto.LoginRequest;
import com.saaskit.starter.dto.RegisterRequest;
import com.saaskit.starter.model.User;
import com.saaskit.starter.repository.UserRepository;
import com.saaskit.starter.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    // InjectMocks will automatically inject all @Mock dependencies into our AuthService
    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        RegisterRequest request = new RegisterRequest("newuser@test.com", "password123");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword123");

        // Act
        authService.register(request);

        // Assert: save must be called exactly once
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenRegisteringExistingEmail() {
        // Arrange
        RegisterRequest request = new RegisterRequest("existing@test.com", "password123");

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            authService.register(request);
        });

        assertEquals("Email already exists", exception.getMessage());

        // Assert: save must never be called
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("user@test.com", "password123");
        User mockUser = new User("user@test.com", "encodedPassword", "USER");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(mockUser));
        when(jwtUtils.generateToken("user@test.com")).thenReturn("fake-jwt-token");
        when(jwtUtils.generateRefreshToken("user@test.com")).thenReturn("fake-refresh-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.token());
        assertEquals("fake-refresh-token", response.refreshToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateToken("user@test.com");
        verify(jwtUtils, times(1)).generateRefreshToken("user@test.com");
    }

    @Test
    void shouldThrowExceptionWhenLoginUserNotFoundAfterAuth() {
        LoginRequest request = new LoginRequest("ghost@test.com", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authService.login(request);
        });

        assertEquals("Invalid email or password", exception.getMessage());
        verify(jwtUtils, never()).generateToken(anyString());
        verify(jwtUtils, never()).generateRefreshToken(anyString());
    }

    @Test
    void shouldRefreshTokensSuccessfully() {
        String refreshToken = "valid-refresh-token";
        String email = "user@test.com";
        User mockUser = new User(email, "encodedPassword", "USER");

        when(jwtUtils.isRefreshToken(refreshToken)).thenReturn(true);
        when(jwtUtils.extractEmail(refreshToken)).thenReturn(email);
        when(jwtUtils.isTokenValid(refreshToken, email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(jwtUtils.generateToken(email)).thenReturn("new-access-token");
        when(jwtUtils.generateRefreshToken(email)).thenReturn("new-refresh-token");

        AuthResponse response = authService.refresh(refreshToken);

        assertNotNull(response);
        assertEquals("new-access-token", response.token());
        assertEquals("new-refresh-token", response.refreshToken());
    }

    @Test
    void shouldThrowWhenRefreshCalledWithAccessToken() {
        String accessToken = "not-a-refresh-token";

        when(jwtUtils.isRefreshToken(accessToken)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.refresh(accessToken);
        });

        assertEquals("Not a refresh token", exception.getMessage());
        verify(jwtUtils, never()).generateToken(anyString());
    }

    @Test
    void shouldThrowWhenRefreshTokenIsExpired() {
        String expiredToken = "expired-refresh-token";
        String email = "user@test.com";

        when(jwtUtils.isRefreshToken(expiredToken)).thenReturn(true);
        when(jwtUtils.extractEmail(expiredToken)).thenReturn(email);
        when(jwtUtils.isTokenValid(expiredToken, email)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.refresh(expiredToken);
        });

        assertEquals("Refresh token expired or invalid", exception.getMessage());
        verify(jwtUtils, never()).generateToken(anyString());
    }
}