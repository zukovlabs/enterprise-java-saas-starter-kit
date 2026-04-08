package com.saaskit.starter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saaskit.starter.dto.AuthResponse;
import com.saaskit.starter.dto.LoginRequest;
import com.saaskit.starter.dto.RegisterRequest;
import com.saaskit.starter.service.AuthService;
import com.saaskit.starter.security.JwtUtils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private DataSource dataSource;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest("test@example.com", "password123");

        doNothing().when(authService).register(any(RegisterRequest.class));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void shouldReturnBadRequestWhenEmailExists() throws Exception {
        RegisterRequest request = new RegisterRequest("existing@example.com", "password123");

        doThrow(new IllegalStateException("Email already exists"))
                .when(authService).register(any(RegisterRequest.class));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email already exists"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        AuthResponse mockResponse = new AuthResponse("fake-jwt-token", "fake-refresh-token");

        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("fake-refresh-token"));
    }

    @Test
    void shouldReturn401OnInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest("wrong@example.com", "wrongpass");

        doThrow(new RuntimeException("Invalid email or password"))
                .when(authService).login(any(LoginRequest.class));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid email or password"));
    }

    @Test
    void shouldRefreshTokensSuccessfully() throws Exception {
        AuthResponse mockResponse = new AuthResponse("new-jwt-token", "new-refresh-token");

        when(authService.refresh("valid-refresh-token")).thenReturn(mockResponse);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"valid-refresh-token\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("new-jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));
    }

    @Test
    void shouldReturn401WhenRefreshTokenIsInvalid() throws Exception {
        doThrow(new IllegalArgumentException("Refresh token expired or invalid"))
                .when(authService).refresh("expired-token");

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"expired-token\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid or expired refresh token"));
    }

    @Test
    void shouldReturn400WhenRefreshTokenIsMissing() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("refreshToken is required"));
    }
}