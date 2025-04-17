package com.payme.token_provider.component;

import com.payme.token_provider.ds.PublicKeyHistory;
import com.payme.token_provider.dto.PublicKeyDto;
import com.payme.token_provider.model.RecentPublicKeys;
import com.payme.token_provider.repository.SigningKeyRepository;
import com.payme.token_provider.constant.KeyAlgorithm;
import com.payme.token_provider.entity.SigningKey;
import com.payme.token_provider.util.KeyPairProvider;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

/**
 * Responsible for generating, rotating, and managing the application's signing key pair.
 * <li>The current private key is used for token signing.</li>
 *
 * <li>The public key history enables clients to validate tokens across key rotations.</li>
 *
 * <li>Keys are persisted for traceability.</li>
 */
@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class SigningKeyManager {
    private static final int REFRESH_INTERVAL_MILLISECONDS = 30 * 60 * 1000;
    private static final int KEY_SIZE_BITS = 2048;
    private static final int KEY_VALIDITY_MINUTES = 31;

    @Getter(AccessLevel.NONE)
    private final SigningKeyRepository signingKeyRepository;
    private final PublicKeyHistory publicKeyHistory;

    private volatile KeyPairHolder keyPairHolder;

    /**
     * Returns the active private key for signing operations.
     */
    public PrivateKey getPrivateKey(){
        return keyPairHolder.privateKey();
    }

/**
 * Retrieves the most recent public key and, if available, the previous one.
 * This allows clients to handle key rollover gracefully.
 *
 * @return RecentPublicKeys object containing the current and previous public keys.
 * @throws IllegalStateException if no keys have been initialized yet.
 */
    public RecentPublicKeys getCurrentAndPreviousPublicKeys(){
        if(keyPairHolder == null){
            throw new IllegalStateException("Failed to initialize public key. ");
        }

        List<String> keys = publicKeyHistory.getKeyHistory();

        return switch (keys.size()){
            case 0 -> throw new IllegalStateException("No public keys available in history. ");
            case 1 -> new RecentPublicKeys(keys.getLast(), null);
            default -> {
                int previousKeyIndex = keys.size() - 2;
                yield new RecentPublicKeys(
                        keys.getLast(),
                        keys.get(previousKeyIndex)
                );
            }
        };

    }

    /**
     * Redundant but ensures intention is clear; this is what needs to be sent to the client.*/
    public PublicKeyDto getPublicKeyResponse(){
        RecentPublicKeys recentPublicKeys = getCurrentAndPreviousPublicKeys();
        return new PublicKeyDto(
                recentPublicKeys.currentPublicKey(),
                recentPublicKeys.previousPublicKey()
        );
    }

    /**
     *  Initializes a new Key Pair on start up. */
    @PostConstruct
    private void initializeSigningKey(){
        try {
            refreshKeys();

            final String publicKey = encodeToString(keyPairHolder.publicKey());
            persistSigningKey(publicKey, KeyAlgorithm.RSA);
            publicKeyHistory.addKey(publicKey);

            log.info("Initial signing key set and persisted. ");
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to initialize signing key.", e);
        }
    }

    /** Updates the Key Pair at a desired time interval. */
    @Scheduled(fixedDelay = REFRESH_INTERVAL_MILLISECONDS)
    private void scheduledKeyRefresh(){
        try {
            refreshKeys();

            final String publicKey = encodeToString(keyPairHolder.publicKey());
            persistSigningKey(publicKey, KeyAlgorithm.RSA);
            publicKeyHistory.addKey(publicKey);

            log.info("Signing key updated and persisted. ");
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to refresh signing key. ");
        }
    }

    private void refreshKeys() throws NoSuchAlgorithmException{
        KeyPair keyPair = KeyPairProvider.generateKeyPair(KEY_SIZE_BITS, "RSA");
        this.keyPairHolder = new KeyPairHolder(keyPair.getPublic(), keyPair.getPrivate());
    }

    private void persistSigningKey(String publicKey, KeyAlgorithm keyAlgorithm){
        SigningKey signingKey = SigningKey.builder()
                .publicKey(publicKey)
                .expiresAt(LocalDateTime.now().plusMinutes(KEY_VALIDITY_MINUTES))
                .keyAlgorithm(keyAlgorithm)
                .build();
        signingKeyRepository.save(signingKey);
    }

    // PublicKey and PrivateKey extend AsymmetricKey
    private String encodeToString(PublicKey publicKey){
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    private record KeyPairHolder(
            PublicKey publicKey,
            PrivateKey privateKey
    ) {
    }

}
