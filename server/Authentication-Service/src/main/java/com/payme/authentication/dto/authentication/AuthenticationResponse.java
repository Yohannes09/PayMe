package com.payme.authentication.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Builder
public record AuthenticationResponse(
        @Schema(description = "Short-lived token used to access protected resources. ")
        String accessToken,

        @Schema(description = "Long-lived token used to fetch a new access token. ")
        String refreshToken,

        @Schema(description = "ID of the authenticated user. ")
        UUID userId
){}
