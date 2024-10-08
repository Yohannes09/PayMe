package com.tenmo.mapper;

import com.tenmo.authentication.dto.RegisterDto;
import com.tenmo.dto.user.UserDto;
import com.tenmo.entity.User;

public class UserMapper {

    public static UserDto mapUserToDto(User user){
        return UserDto.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public static User mapRegisterDtoToUser(RegisterDto dto){
        return  User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}
