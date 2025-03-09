package com.payme.app.authentication.dto;

import com.payme.app.constants.PaymeRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterDto {

    @NotNull private String firstName;

    @NotNull private String lastName;

    @NotNull private String username;

    @Email
    @NotNull private String email;


    @NotNull private String password;

    private PaymeRoles paymeRoles;
}
