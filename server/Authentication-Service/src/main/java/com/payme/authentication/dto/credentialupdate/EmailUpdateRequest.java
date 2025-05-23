package com.payme.authentication.dto.credentialupdate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record EmailUpdateRequest(
        @NotBlank
        UUID id,

        @NotBlank
        @Email
        String newEmail
){}
