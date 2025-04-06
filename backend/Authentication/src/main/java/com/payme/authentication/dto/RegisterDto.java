package com.payme.authentication.dto;

import com.payme.authentication.constant.ValidationPattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterDto(
        @NotNull
        @Pattern(
                regexp = ValidationPattern.USERNAME_PATTERN,
                message = "Invalid username format. "
        )
        String username,

        @NotNull
        @Email(message = "Invalid email format. ")
        String email,

        @NotNull
        @Pattern(
                regexp = ValidationPattern.PASSWORD_PATTERN,
                message = "Passwords must contain at least 1: Capital letter, Number, and special symbol."
        )
        String password
) {

}
