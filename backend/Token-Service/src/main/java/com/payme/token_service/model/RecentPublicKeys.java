package com.payme.token_service.model;

public record RecentPublicKeys(
        String currentPublicKey,
        String previousPublicKey
) {
}
