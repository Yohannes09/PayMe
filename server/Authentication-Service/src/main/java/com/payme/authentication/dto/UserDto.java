package com.payme.authentication.dto;

import com.payme.authentication.entity.Role;
import com.payme.authentication.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record UserDto(
        UUID id,

        String username,

        String password,

        String email,

        Set<String> roles,

        LocalDateTime updatedAt,

        boolean accountNonExpired,

        boolean accountNonLocked,

        boolean credentialsNonExpired,

        boolean enabled
){
    public static UserDto entityToDto(User user){
        Set<String> roles = user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(roles)
                .accountNonExpired(user.isAccountNonExpired())
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .build();
    }
}
