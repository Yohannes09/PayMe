package com.payme.token_provider.dto;

public record AuthenticationTokensDto(
        String accessToken,
        String refreshToken
) {
}
