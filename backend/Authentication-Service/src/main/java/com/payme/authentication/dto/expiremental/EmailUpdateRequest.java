package com.payme.authentication.dto.expiremental;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record EmailUpdateRequest(
        @NotBlank
        UUID id,

        @NotBlank
        @Email
        String newEmail
) implements CredentialUpdateRequest {

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getNewCredential() {
        return newEmail;
    }

}
