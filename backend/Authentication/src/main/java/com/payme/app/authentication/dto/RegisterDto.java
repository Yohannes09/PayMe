package com.payme.app.authentication.dto;

import com.payme.app.constants.ValidationPattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterDto(
        @NotNull String firstName,
        @NotNull String lastName,

        @NotNull
        @Pattern(
                regexp = "^[a-zA-Z0-9]{5,15}$",
                message = "Invalid username format. "
        )
        String username,

        @NotNull @Email(message = "Invalid email format. ")
        String email,

        @NotNull
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9!@#$%^&*]).{8,}$",
                message = "Passwords must contain at least 1: Capital letter, Number, and special symbol."
        )
        String password
) {
}
