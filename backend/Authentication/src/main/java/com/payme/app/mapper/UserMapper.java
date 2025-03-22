package com.payme.app.mapper;

import com.payme.app.authentication.util.UserPrincipal;
import com.payme.app.dto.user.UserDto;
import com.payme.app.entity.User;

public class UserMapper {

    public static UserDto mapUserToDto(User user){
        return UserDto.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .accounts(user.getAccounts().stream().map(AccountMapper::mapAccountToDto).toList())
                .build();
    }

    public static UserPrincipal mapUserToPrincipal(User user){
        return UserPrincipal.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .accounts(user.getAccounts().stream().map(AccountMapper::mapAccountToDto).toList())
                .build();
    }

}
