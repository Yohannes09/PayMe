package com.payme.app.authentication.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class LoginDto {
   @NotNull
   private String usernameOrEmail;
   @NotNull
   private String password;
}
