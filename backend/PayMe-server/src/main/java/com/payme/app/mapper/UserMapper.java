package com.payme.app.mapper;

import com.payme.app.dto.user.UserDto;
import com.payme.app.entity.User;

public class UserMapper {

    public static UserDto mapUserToDto(User user){
        return UserDto.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

}
