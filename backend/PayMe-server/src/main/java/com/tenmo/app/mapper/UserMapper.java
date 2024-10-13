package com.tenmo.app.mapper;

import com.tenmo.app.dto.user.UserDto;
import com.tenmo.app.entity.User;

public class UserMapper {

    public static UserDto mapUserToDto(User user){
        return UserDto.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRoles())
                .build();
    }

}
