package com.payme.authentication.mapper;

import com.payme.authentication.dto.UserDto;
import com.payme.authentication.entity.User;

public class UserMapper {

    public static UserDto mapUserToDto(User user){
        return UserDto.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

}
