package com.payme.token_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InternalTokenDto(
        @NotBlank(message = "Service name or ID must not be blank.")
        @Size(min = 4, message = "Service name or ID must be at least 4 characters long.")
                        String serviceNameOrId
){}
