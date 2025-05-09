package com.payme.authentication.dto.CredentialUpdate;

import com.payme.authentication.constant.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record PasswordUpdateRequest(
        @NotBlank
        UUID id,

        @NotBlank(message = ValidationConstants.PASSWORD_VALIDATION_MESSAGE)
        @Pattern(
                regexp = ValidationConstants.PASSWORD_PATTERN,
                message = ValidationConstants.PASSWORD_VALIDATION_MESSAGE
        )
        String newPassword
){}
