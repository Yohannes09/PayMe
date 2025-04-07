package com.payme.internal_authentication.dto;

import java.util.Optional;

public record AuthenticationRequestDto(
        String serviceName,
        String baseUrl,
        String endpoint,
        String description
) {
}
