package com.payme.token_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserTokenDto(
        @Size(min = 6, message = "Username or ID must be at least 6 characters long")
        @NotBlank(message = "A username or ID are required.")
        String usernameOrId,

        @NotEmpty(message = "Must include at least 1 role or scope")
        Set<
                @NotBlank(message = "Roles must not be null or blank. ")
                @Size(min = 4, message = "Roles must at least 4 characters long. ")
                        String> roles
){}
