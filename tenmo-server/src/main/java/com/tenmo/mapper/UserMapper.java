package com.tenmo.mapper;

import com.tenmo.dto.authentication.RegisterUserDto;
import com.tenmo.dto.user.UserDto;
import com.tenmo.entity.User;

public class UserMapper {

    public static UserDto mapUserToDto(User user){
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }

    public static User mapRegisterDtoToUser(RegisterUserDto dto){
        return new User(
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword()
        );
    }
}
