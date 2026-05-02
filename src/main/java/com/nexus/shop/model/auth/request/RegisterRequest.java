package com.nexus.shop.model.auth.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @jakarta.validation.constraints.Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters long")
        String username,
        @NotBlank(message = "Password is required")
        @jakarta.validation.constraints.Size(min = 6, message = "Password must be at least 6 characters long")
        String password
) {}