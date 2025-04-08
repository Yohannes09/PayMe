package com.payme.internal_authentication.dto;

public record AuthenticationRequestDto(
        String clientName,
        String baseUrl,
        String endpoint,
        String description
) {
}
