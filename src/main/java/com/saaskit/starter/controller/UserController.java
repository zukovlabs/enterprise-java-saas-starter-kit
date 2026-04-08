package com.saaskit.starter.controller;

import com.saaskit.starter.dto.ChangePasswordRequest;
import com.saaskit.starter.dto.UpdateProfileRequest;
import com.saaskit.starter.model.User;
import com.saaskit.starter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserByEmail(authentication.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody @Valid UpdateProfileRequest request, Authentication authentication) {
        return ResponseEntity.ok(userService.updateProfile(authentication.getName(), request));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request, Authentication authentication) {
        userService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }
}