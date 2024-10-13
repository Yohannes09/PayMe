package com.tenmo.app.authentication.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthenticationResponseDto {
    private String token;
    private String firstName;
    private String lastName;
    private String email;
    private UUID userId;
}
