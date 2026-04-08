package com.saaskit.starter.service;

import com.saaskit.starter.dto.ChangePasswordRequest;
import com.saaskit.starter.dto.UpdateProfileRequest;
import com.saaskit.starter.model.User;
import com.saaskit.starter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldGetUserByEmailSuccessfully() {
        User mockUser = new User("test@example.com", "pass", "USER");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        User result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.getUserByEmail("notfound@example.com");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

    @Test
    void shouldUpdateProfileSuccessfully() {
        User mockUser = new User("test@example.com", "pass", "USER");
        UpdateProfileRequest request = new UpdateProfileRequest("John", "Doe");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.updateProfile("test@example.com", request);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        User mockUser = new User("test@example.com", "encodedOldPass", "USER");
        ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass123")).thenReturn("encodedNewPass");

        userService.changePassword("test@example.com", request);

        assertEquals("encodedNewPass", mockUser.getPassword());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIsInvalid() {
        User mockUser = new User("test@example.com", "encodedOldPass", "USER");
        ChangePasswordRequest request = new ChangePasswordRequest("wrongOldPass", "newPass123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        when(passwordEncoder.matches("wrongOldPass", "encodedOldPass")).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.changePassword("test@example.com", request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid current password", exception.getReason());

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}