package com.payme.authentication.user_micro_serv.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String email
) {
}
