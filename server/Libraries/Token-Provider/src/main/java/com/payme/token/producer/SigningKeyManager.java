package com.payme.token.producer;

import com.payme.token.model.KeyRecord;
import com.payme.token.repository.PublicKeyStore;
import com.payme.token.exception.KeyInitializationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.*;
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
@RequiredArgsConstructor
public class SigningKeyManager <T extends KeyRecord>{
    private final PublicKeyStore<T> publicKeyStore;
    private final KeyFactory<T> keyFactory;
    private final TokenConfigurationProperties configuration;

    // CONCERN: If rotation happens frequently, validating tokens might become challenging.
    //
    // This should be injected, as the consumer should choose how long and what is storing the
    // public keys. It'd be better for testing. Leaving as is for now.
    private final PublicKeyHistory publicKeyHistory = new PublicKeyHistory();

    private volatile ActiveKeyPair activeKeyPair;


    /**
     * Returns a read-only snapshot of recent public keys used for token signing.
     * <p>
     * Includes the current public key and previously used keys to allow clients
     * to verify tokens issued before a key rotation.
     *
     * @return an immutable {@link List} of base64-encoded public keys.
     * @throws KeyInitializationException if no signing key has been initialized.
     */
    public List<String> getPublicKeyHistory(){
        validateKeyIsInitialized();
        return Collections.unmodifiableList(publicKeyHistory.getKeyHistory());
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
     * Manages the signing keys used for token generation.
     * <p>
     * Responsibilities:
     * <ul>
     *     <li>Generates and rotates the key pair used for signing and validating tokens.</li>
     *     <li>Persists the public key and tracks historical keys for verification support.</li>
     *     <li>Provides access to the currently active signing key and metadata.</li>
     * </ul>
     * <p>
     * Consumers are responsible for calling {@link #rotateSigningKey()} to initialize or rotate keys
     * based on their security policy (e.g., after the configured rotation interval).
     * The manager is thread-safe and supports pluggable key formats via {@link KeyFactory}.
     */
    public synchronized void rotateSigningKey(){
        try {

            KeyPair keyPair = generateKeyPair(
                    configuration.getSigning().getKeySize(),
                    configuration.getSigning().getAlgorithm()
            );

            String publicKey = base64Encode(keyPair.getPublic());
            publicKeyHistory.addKey(publicKey);

            String signingAlgorithm = configuration.getSigning().getAlgorithm();
            T keyRecord = persistPublicKey(publicKey, signingAlgorithm);

            // Null the current KeyPair to ensure an old KeyPair in memory isn't used
            this.activeKeyPair = null;
            this.activeKeyPair = new ActiveKeyPair(keyRecord, keyPair.getPrivate());

            log.info("Rotating signing key with algorithm {}", signingAlgorithm);

        } catch (Exception e) {
            log.error("Failed to rotate signing key. {}", e.getMessage(), e);
            throw new KeyInitializationException("Failed to rotate signing key. " + e.getMessage());
        }

    }

    private KeyPair generateKeyPair(int sizeInBits, String algorithmType) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithmType);
        keyPairGenerator.initialize(sizeInBits);

        return keyPairGenerator.generateKeyPair();
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
    public interface KeyFactory<T> {
        T create(
                String publicKey,
                String signingAlgorithm,
                LocalDateTime issuedAt,
                LocalDateTime expiresAt
        );

    }


    /**
     * PublicKeyHistory is a thread-safe data structure that stores a desired fixed-size history of public keys.
     */
    public static class PublicKeyHistory {
        private static final int DEFAULT_MAX_SIZE = 5;

        private final Deque<String> keyHistory = new ConcurrentLinkedDeque<>();

        public void addKey(String key){
            if(keyHistory.size() >= DEFAULT_MAX_SIZE){
                keyHistory.pollFirst();
            }
            keyHistory.addLast(key);
        }

        public List<String> getKeyHistory(){
            return new ArrayList<>(keyHistory);
        }

    }

}

