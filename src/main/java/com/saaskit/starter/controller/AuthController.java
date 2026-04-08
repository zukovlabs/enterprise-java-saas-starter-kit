package com.saaskit.starter.controller;

import com.saaskit.starter.dto.LoginRequest;
import com.saaskit.starter.dto.RegisterRequest;
import com.saaskit.starter.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid email or password"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "refreshToken is required"));
        }
        try {
            return ResponseEntity.ok(authService.refresh(refreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid or expired refresh token"));
        }
    }
}