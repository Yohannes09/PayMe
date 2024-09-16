package com.techelevator.tenmo.dto;

import com.techelevator.tenmo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponseDto {
    private String token;
    private User user;

}
