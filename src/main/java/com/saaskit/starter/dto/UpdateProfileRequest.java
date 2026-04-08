package com.saaskit.starter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name too long")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name too long")
        String lastName
) {}