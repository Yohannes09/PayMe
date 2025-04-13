package com.payme.internal_authentication.dto;

public record ClientRegisterDto(
        String name,
        String baseUrl,
        String description
) {
}
