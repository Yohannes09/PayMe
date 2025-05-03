package com.payme.token_service.component.signing_key;

import com.payme.token_service.data_structure.PublicKeyHistory;
import com.payme.token_service.dto.PublicKeyDto;
import com.payme.token_service.exception.KeyInitializationException;
import com.payme.token_service.model.RecentPublicKeys;
import com.payme.token_service.repository.PublicKeyRecordRepository;
import com.payme.token_service.entity.PublicKeyRecord;
import com.payme.token_service.util.KeyPairProvider;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.cdi.Eager;
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
@Component
@Slf4j
@RequiredArgsConstructor
public class SigningKeyManager {
    private static final int MINUTE_IN_MS = 1000 * 60;
    private static final int TOKEN_ROTATION_INTERVAL = 30 * MINUTE_IN_MS;
    private static final int KEY_SIZE_BITS = 2048;
    private static final int KEY_VALIDITY_MINUTES = 30;

    private final PublicKeyRecordRepository publicKeyRecordRepository;
    private final PublicKeyHistory publicKeyHistory;

    private volatile ActiveSigningKey activeSigningKey;


    /**
     * Retrieves the most recent public key and, if available, the previous one.
     * This allows clients to handle key rollover gracefully.
     *
     * @return RecentPublicKeys object containing the current and previous public keys.
     * @throws IllegalStateException if no keys have been initialized yet.
     */
    public RecentPublicKeys getCurrentAndPreviousPublicKeys(){
        if(activeSigningKey == null){
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

    // Used for signing keys.
    public PrivateKey getActivePrivateKey(){
        if(activeSigningKey == null){
            throw new KeyInitializationException();
        }
        return activeSigningKey.privateKey();
    }

    // Used by TokenProvider to add the singing key's ID to the token header.
    public PublicKeyRecord getActiveSigningKey(){
        if(activeSigningKey == null){
            throw new KeyInitializationException();
        }
        return activeSigningKey.publicKeyRecord();
    }

    // Client response
    public PublicKeyDto getPublicKeyResponse(){
        RecentPublicKeys recentPublicKeys = getCurrentAndPreviousPublicKeys();
        return new PublicKeyDto(
                recentPublicKeys.currentPublicKey(),
                recentPublicKeys.previousPublicKey()
        );
    }


    @Scheduled(fixedDelay = TOKEN_ROTATION_INTERVAL)
    private void rotateSigningKey(){
        try {
            KeyPair keyPair = KeyPairProvider.generateKeyPair(
                    KEY_SIZE_BITS,
                    "RSA"
            );

            final String encodedPublicKey = encodeToString(keyPair.getPublic());
            PublicKeyRecord publicKeyRecord = persistSigningKey(encodedPublicKey, SignatureAlgorithm.RS256);
            publicKeyHistory.addKey(encodedPublicKey);

            this.activeSigningKey = new ActiveSigningKey(
                    publicKeyRecord,
                    keyPair.getPublic(),
                    keyPair.getPrivate()
            );

            log.info("Signing key updated and persisted. ");
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to refresh signing key. ");
        }

    }

    private PublicKeyRecord persistSigningKey(String publicKey, SignatureAlgorithm signatureAlgorithm){
        PublicKeyRecord publicKeyRecord = PublicKeyRecord.builder()
                .publicKey(publicKey)
                .expiresAt(LocalDateTime.now().plusMinutes(KEY_VALIDITY_MINUTES))
                .signatureAlgorithm(signatureAlgorithm)
                .build();
        return publicKeyRecordRepository.save(publicKeyRecord);
    }

    // PublicKey and PrivateKey extend AsymmetricKey
    private String encodeToString(PublicKey publicKey){
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    private record ActiveSigningKey(
            PublicKeyRecord publicKeyRecord,
            PublicKey publicKey,
            PrivateKey privateKey
    ) {}

}
