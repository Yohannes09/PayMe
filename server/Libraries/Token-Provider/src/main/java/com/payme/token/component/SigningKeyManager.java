package com.payme.token.component;

import com.payme.token.model.KeyRecord;
import com.payme.token.repository.PublicKeyStore;
import com.payme.token.exception.KeyInitializationException;
import com.payme.token.util.KeyPairProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Manages the signing keys used for token generation.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Generates and rotates the key pair used for signing and validating tokens.</li>
 *     <li>Persists the public key and tracks historical keys for verification support.</li>
 *     <li>Provides access to the currently active signing key and metadata.</li>
 * </ul>
 * <p>
 * The manager is designed to be thread-safe and supports pluggable key formats via {@link KeyFactory}.
 *
 * @param <T> The concrete type of {@link KeyRecord} used to store public key metadata.
 */
@Component
@Slf4j
@Builder
@RequiredArgsConstructor
public class SigningKeyManager <T extends KeyRecord>{

    private final PublicKeyStore<T> publicKeyStore;
    private final KeyFactory<T> keyFactory;
    private final TokenConfigurationProperties configuration;
    private final int publicKeyHistoryLength;

    private final PublicKeyHistory publicKeyHistory = new PublicKeyHistory(publicKeyHistoryLength);

    private volatile ActiveKeyPair activeKeyPair;


    /**
     * Returns a read-only snapshot of recent public keys used for token signing.
     * <p>
     * Includes the current public key and previously used keys to allow clients
     * to verify tokens issued before a key rotation.
     *
     * @return an immutable {@link Set} of base64-encoded public keys.
     * @throws KeyInitializationException if no signing key has been initialized.
     */
    public Set<String> getPublicKeyHistory(){
        validateKeyIsInitialized();

        return Set.copyOf(publicKeyHistory.getKeyHistory());
    }


    /**
     * Retrieves the current private key used for signing tokens.
     * <p>
     * This key must be kept secure and is used internally to sign authentication tokens.
     *
     * @return the active {@link PrivateKey} used for signing.
     * @throws KeyInitializationException if the signing key has not been initialized.
     */
    protected PrivateKey getActiveSigningKey(){
        validateKeyIsInitialized();
        return activeKeyPair.getPrivateKey();
    }


    /**
     * Returns the active public key metadata record.
     * <p>
     * This key corresponds to the private signing key and is shared with clients
     * to verify signatures during key rotation periods.
     *
     * @return the current active public key record of type {@code T}.
     * @throws KeyInitializationException if the public key has not been initialized.
     */
    public T getActivePublicKey(){
        validateKeyIsInitialized();
        return activeKeyPair.getPublicKeyRecord();
    }


    /**
     * Generates a new key pair and updates the active signing key.
     * <p>
     * The new public key is persisted using the provided {@link PublicKeyStore},
     * and the corresponding private key is stored in memory for token signing.
     * Previous public keys are preserved in a history to support token verification.
     * <p>
     * This method is synchronized to prevent concurrent key rotations.
     *
     * @throws KeyInitializationException if key generation fails.
     */
    public synchronized void rotateSigningKey(){
        try {
            KeyPair keyPair = KeyPairProvider.generateKeyPair(
                    configuration.getSigning().getKeySizeBits(),
                    configuration.getSigning().getAlgorithm()
            );

            String publicKey = base64Encode(keyPair.getPublic());
            publicKeyHistory.addKey(publicKey);

            String signingAlgorithm = configuration.getSigning().getAlgorithm();
            T keyRecord = persistPublicKey(publicKey, signingAlgorithm);

            this.activeKeyPair = new ActiveKeyPair(keyRecord, keyPair.getPrivate());

            log.info("Rotating sigining key with algorithm {}", signingAlgorithm);

        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to rotate signing key. {}", e.getMessage());
            throw new KeyInitializationException(e.getMessage());
        }

    }


    /**
     * Creates and persists a new public key record with issued and expiry timestamps.
     *
     * @param publicKey base64-encoded public key string.
     * @param signatureAlgorithm the algorithm used for signing keys.
     * @return the persisted key record of type {@code T}.
     */
    private T persistPublicKey(String publicKey, String signatureAlgorithm){
        int keyRotationIntervalMinutes = configuration
                .getSigning()
                .getRotationIntervalMinutes();

        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(keyRotationIntervalMinutes);

        return publicKeyStore.save(
                keyFactory.create(publicKey, signatureAlgorithm, issuedAt, expiresAt)
        );
    }

    /**
     * Encodes the given {@link PublicKey} into a base64 string.
     *
     * @param publicKey the public key to encode.
     * @return the base64-encoded string representation of the public key.
     */
    private String base64Encode(PublicKey publicKey){
        return Base64.getEncoder()
                .encodeToString(publicKey.getEncoded());
    }


    /**
     * Validates that the signing key and public key history have been initialized.
     * Throws {@link KeyInitializationException} if validation fails.
     */
    private void validateKeyIsInitialized(){
        if(activeKeyPair == null || publicKeyHistory.getKeyHistory().isEmpty()){
            log.error("Application failed to start. ");
            throw new KeyInitializationException("Failed to initialize public key. ");
        }
    }


    /**
     * Holds the active signing key pair, including public key metadata and the private key.
     */
    @Getter
    @RequiredArgsConstructor
    private class ActiveKeyPair{
        private final T publicKeyRecord;
        private final PrivateKey privateKey;
    }


    /**
     * Functional interface to create a key record instance with metadata.
     *
     * @param <T> the type of {@link KeyRecord}.
     */
    @FunctionalInterface
    public static interface KeyFactory<T> {
        T create(
                String publicKey, String signingAlgorithm, LocalDateTime issuedAt, LocalDateTime expiresAt
        );

    }


    /**
     * PublicKeyHistory is a thread-safe data structure that stores a desired fixed-size history of public keys.
     */
    @RequiredArgsConstructor
    public static class PublicKeyHistory {
        private final int maxSize;

        private Deque<String> keyHistory = new ConcurrentLinkedDeque<>();

        public void addKey(String key){
            if(keyHistory.size() >= maxSize){
                keyHistory.pollFirst();
            }
            keyHistory.addLast(key);
        }

        public List<String> getKeyHistory(){
            return new ArrayList<>(keyHistory);
        }

    }

}

