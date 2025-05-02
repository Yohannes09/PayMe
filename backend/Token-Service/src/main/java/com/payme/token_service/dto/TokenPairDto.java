package com.payme.token_service.dto;

public record TokenPairDto(
        String accessToken,
        String refreshToken
) {
}
