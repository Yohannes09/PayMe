package com.payme.gateway.dto;

import java.util.UUID;

public record AuthenticationResponseDto(String token, String firstName, String lastName, UUID userId) {
}
