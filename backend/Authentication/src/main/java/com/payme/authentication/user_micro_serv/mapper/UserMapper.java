package com.payme.authentication.user_micro_serv.mapper;

import com.payme.authentication.dto.RegisterDto;
import com.payme.authentication.entity.SecurityUser;
import com.payme.authentication.user_micro_serv.dto.UserDto;
import com.payme.authentication.user_micro_serv.entity.User;

import java.util.function.Function;

public class UserMapper {

    public static Function<? super @org.jetbrains.annotations.NotNull SecurityUser, ? extends UserDto> mapUserToDto(User user){
        return UserDto.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public static User createUser(RegisterDto registerDto){

    }

}
