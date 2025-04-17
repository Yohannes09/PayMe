package com.payme.token_provider.dto;

import jakarta.annotation.Nullable;

public record PublicKeyDto(
        String currentPublicKey,
        @Nullable
        String previousPublicKey
) {
}
