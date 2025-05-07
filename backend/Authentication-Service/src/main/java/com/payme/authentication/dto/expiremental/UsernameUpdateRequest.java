package com.payme.authentication.dto.expiremental;

import com.payme.authentication.constant.ValidationPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record UsernameUpdateRequest (
        @NotBlank
        UUID id,

        @NotBlank(message = ValidationPattern.USERNAME_VALIDATION_MESSAGE)
        @Pattern(
                regexp = ValidationPattern.USERNAME_PATTERN,
                message = ValidationPattern.USERNAME_VALIDATION_MESSAGE
        )
        String newUsername
) implements CredentialUpdateRequest{

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getNewCredential() {
        return newUsername;
    }

}
