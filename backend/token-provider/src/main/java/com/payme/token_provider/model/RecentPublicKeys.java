package com.payme.token_provider.model;

public record RecentPublicKeys(
        String currentPublicKey,
        String previousPublicKey
) {
}
