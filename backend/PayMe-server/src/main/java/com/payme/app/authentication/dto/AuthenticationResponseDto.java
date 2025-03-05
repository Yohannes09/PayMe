package com.payme.app.authentication.dto;

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

    // eventually this will need to be annotated with @Email and other annotations that enforce
    // each field's requirements.
}
