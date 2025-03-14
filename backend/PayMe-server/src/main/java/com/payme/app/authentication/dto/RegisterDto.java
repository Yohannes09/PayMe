package com.payme.app.authentication.dto;

import com.payme.app.constants.ValidationPattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterDto {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{5,15}$", message = "Invalid username format. ")
    private String username;

    @NotNull
    @Email(message = "Invalid email format. ")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9!@#$%^&*]).{8,}$",
            message = "Passwords must contain at least 1: Capital letter, Number, and special symbol.")
    private String password;
}
