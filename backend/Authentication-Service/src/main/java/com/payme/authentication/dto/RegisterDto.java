package com.payme.authentication.dto;

import com.payme.authentication.constant.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterDto(
        @NotBlank(message = "Username cannot be blank.")
        @Pattern(
                regexp = ValidationConstants.USERNAME_PATTERN,
                message = "Invalid username format. "
        )
        String username,

        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Invalid email format. ")
        String email,

        @NotBlank(message = "Password cannot be blank.")
        @Pattern(
                regexp = ValidationConstants.PASSWORD_PATTERN,
                message = "Passwords must contain at least 1: Capital letter, Number, and special symbol."
        )
        String password
){}
