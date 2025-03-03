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

    @Size(min = 5, max = 30)
    @NotNull private String username;

    @Email
    @NotNull private String email;

//    @Pattern(regexp = "\"^(?=.*[A-Z])(?=.*[0-9!@#$%^&*]).{6,}$\"",
//            message = "Password must at least 6 characters long and contain special characters. ")
    @NotNull private String password;

    private PaymeRoles paymeRoles;

}
