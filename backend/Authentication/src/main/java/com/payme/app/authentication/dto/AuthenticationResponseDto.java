package com.payme.app.authentication.dto;

import lombok.*;

import java.util.UUID;

@Builder
public record AuthenticationResponseDto(
        String token,
        UUID userId
) {
}
