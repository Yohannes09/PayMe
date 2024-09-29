package com.tenmo.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDto {
   private String usernameOrEmail;
   private String password;

   @Override
   public String toString() {
      return "LoginDto{" +
              "username='" + usernameOrEmail + '\'' +
              ", password='" + password + '\'' +
              '}';
   }
}
