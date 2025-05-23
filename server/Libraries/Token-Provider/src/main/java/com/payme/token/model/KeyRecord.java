package com.payme.token.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface KeyRecord extends Serializable {
    Object getId();

    /**
     * @return Base64-encoded Public Key.*/
    String getPublicKey();

    String getSignatureAlgorithm();

    LocalDateTime getCreatedAt();

    LocalDateTime getExpiresAt();

    LocalDateTime getRevokedAt();
}
