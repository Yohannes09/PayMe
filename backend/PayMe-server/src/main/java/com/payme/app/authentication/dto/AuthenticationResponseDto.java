package com.payme.app.authentication.dto;

import lombok.*;

import java.util.UUID;

//token and userId should be sufficient, no need for other fields.
@AllArgsConstructor
@Getter
@Builder
public class AuthenticationResponseDto {
    private String token;
    private UUID userId;
}
