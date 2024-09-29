package com.tenmo.dto.authentication;

import com.tenmo.util.TenmoRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterUserDto {
    @NotEmpty
    private String username;

    @Pattern(regexp = "\"^(?=.*[A-Z])(?=.*[0-9!@#$%^&*]).{6,}$\"",
            message = "Password must")
    @NotEmpty
    private String password;

    @Email
    private String email;

    private final String role = TenmoRoles.USER.getRole();

}
