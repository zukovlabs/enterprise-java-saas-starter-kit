package com.saaskit.starter.service;

import com.saaskit.starter.dto.ChangePasswordRequest;
import com.saaskit.starter.dto.UpdateProfileRequest;
import com.saaskit.starter.model.User;
import com.saaskit.starter.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User updateProfile(String email, UpdateProfileRequest request) {
        User user = getUserByEmail(email);

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());

        return userRepository.save(user);
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid current password");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}