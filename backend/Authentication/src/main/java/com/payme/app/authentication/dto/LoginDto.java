package com.payme.app.authentication.dto;

import jakarta.validation.constraints.NotNull;

public record LoginDto(
        @NotNull String usernameOrEmail,
        @NotNull String password
) {
}
