package com.payme.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginDto(
        @NotBlank
        @Size
        String usernameOrEmail,
        @NotNull String password
){}
