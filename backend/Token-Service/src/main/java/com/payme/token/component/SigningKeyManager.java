package com.payme.token.component;

import com.payme.token.model.KeyRecord;
import com.payme.token.repository.PublicKeyStore;
import com.payme.token.util.data_structure.PublicKeyHistory;
import com.payme.token.exception.KeyInitializationException;
import com.payme.token.util.KeyPairProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

/**
 * Responsible for generating, rotating, and managing the application's signing key pair.
 * <li>The current private key is used for token signing.</li>
 *
 * <li>The public key history enables clients to validate tokens across key rotations.</li>
 *
 * <li>Keys are persisted for traceability.</li>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SigningKeyManager <T extends KeyRecord>{
    private final PublicKeyStore<T> publicKeyStore;
    private final PublicKeyHistory publicKeyHistory;
    private final TokenConfigurationProperties configuration;
    private final KeyFactory<T> keyFactory;
    private volatile ActiveKeyPair activeKeyPair;


    /**
     * Retrieves the most recent public key and, if available, the previous one.
     * This allows clients to handle key rollover gracefully.
     *
     * @return RecentPublicKeys object containing the current and previous public keys.
     * @throws IllegalStateException if no keys have been initialized yet.
     */
    public Set<String> getPublicKeyHistory(){
        if(activeKeyPair == null || publicKeyHistory.getKeyHistory().isEmpty()){
            throw new IllegalStateException("Failed to initialize public key. ");
        }

        return new HashSet<>(publicKeyHistory.getKeyHistory());
    }

    protected PrivateKey getActiveSigningKey(){
        if(activeKeyPair == null){
            throw new KeyInitializationException();
        }
        return activeKeyPair.privateKey();
    }

    /**
     * PublicKeyRecord contains all necessary Public Key meta-data.
     */
    public KeyRecord getActivePublicKey(){
        if(activeKeyPair == null){
            throw new KeyInitializationException();
        }
        return activeKeyPair.publicKeyRecord();
    }

    public void rotateSigningKey(){
        try {
            KeyPair keyPair = KeyPairProvider.generateKeyPair(
                    configuration.getSigning().getKeySizeBits(),
                    configuration.getSigning().getAlgorithm()
            );

            String publicKey = base64Encode(keyPair.getPublic());

            T keyRecord = persistPublicKey(
                    publicKey,
                    configuration.getSigning().getAlgorithm()
            );

            publicKeyHistory.addKey(publicKey);

            this.activeKeyPair = new ActiveKeyPair(
                    keyRecord,
                    keyPair.getPublic(),
                    keyPair.getPrivate()
            );

        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to refresh signing key. ");
        }

    }

    private T persistPublicKey(String publicKey, String signatureAlgorithm){
        int keyRotationIntervalMinutes = configuration
                .getSigning()
                .getRotationIntervalMinutes();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(keyRotationIntervalMinutes);
        T keyRecord = keyFactory.create(
                publicKey, signatureAlgorithm, LocalDateTime.now(), expiresAt);

        return publicKeyStore.save(keyRecord);
    }

    private String base64Encode(PublicKey publicKey){
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    private record ActiveKeyPair<T>(
            T publicKeyRecord,
            PublicKey publicKey,
            PrivateKey privateKey
    ) {}

    @FunctionalInterface
    public static interface KeyFactory<T extends KeyRecord>{
        T create(
                String publicKey,
                String signingAlgorithm,
                LocalDateTime issuedAt,
                LocalDateTime expiresAt
        );
    }
}

