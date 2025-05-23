package com.payme.authentication.dto.credentialupdate;

import com.payme.authentication.constant.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record UsernameUpdateRequest (
        @NotBlank
        UUID id,

        @NotBlank(message = ValidationConstants.USERNAME_VALIDATION_MESSAGE)
        @Pattern(
                regexp = ValidationConstants.USERNAME_PATTERN,
                message = ValidationConstants.USERNAME_VALIDATION_MESSAGE
        )
        String newUsername
){}
