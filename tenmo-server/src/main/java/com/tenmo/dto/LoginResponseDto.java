package com.tenmo.dto;

import com.tenmo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponseDto {
    private String token;
    private User user;

}
