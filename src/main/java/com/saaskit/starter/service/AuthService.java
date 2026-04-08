package com.saaskit.starter.service;

import com.saaskit.starter.dto.AuthResponse;
import com.saaskit.starter.dto.LoginRequest;
import com.saaskit.starter.dto.RegisterRequest;
import com.saaskit.starter.model.User;
import com.saaskit.starter.repository.UserRepository;
import com.saaskit.starter.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already exists");
        }
        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                "USER"
        );
        userRepository.save(user);
    }

    public AuthResponse refresh(String refreshToken) {
        if (!jwtUtils.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Not a refresh token");
        }
        String email = jwtUtils.extractEmail(refreshToken);
        if (!jwtUtils.isTokenValid(refreshToken, email)) {
            throw new IllegalArgumentException("Refresh token expired or invalid");
        }
        userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String newAccessToken = jwtUtils.generateToken(email);
        String newRefreshToken = jwtUtils.generateRefreshToken(email);
        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        String token = jwtUtils.generateToken(user.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

        return new AuthResponse(token, refreshToken);
    }
}