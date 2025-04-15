package com.payme.token_provider.service;

import com.payme.token_provider.repository.SigningKeyRepository;
import com.payme.token_provider.constant.KeyAlgorithm;
import com.payme.token_provider.entity.SigningKey;
import com.payme.token_provider.util.KeyPairProvider;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class SigningKeyManager {
    private static final int REFRESH_INTERVAL_MILLISECONDS = 30 * 60 * 1000;
    private static final int KEY_SIZE_BITS = 2048;
    private static final int KEY_VALIDITY_MINUTES = 31;

    @Getter(AccessLevel.NONE)
    private final SigningKeyRepository signingKeyRepository;

    private volatile KeyPairHolder keyPairHolder;


    public String getEncodedPublicKey(){
        if(keyPairHolder.publicKey== null){
            throw new IllegalStateException("Failed to initialize public key. ");
        }
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }


    @PostConstruct
    private void initializeSigningKey(){
        try {
            refreshKeys();
            SigningKey signingKey = generate(getEncodedPublicKey(), KeyAlgorithm.RSA);
            signingKeyRepository.save(signingKey);
            log.info("Initial signing key set and persisted. ");
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to initialize signing key.", e);
        }
    }

    @Scheduled(fixedDelay = REFRESH_INTERVAL_MILLISECONDS)
    private void scheduledKeyRefresh(){
        try {
            refreshKeys();
            SigningKey signingKey = generate(getEncodedPublicKey(), KeyAlgorithm.RSA);
            signingKeyRepository.save(signingKey);
            log.info("Signing key updated and persisted. ");
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to refresh signing key. ");
        }
    }

    private void refreshKeys() throws NoSuchAlgorithmException{
        KeyPair keyPair = KeyPairProvider.generateKeyPair(KEY_SIZE_BITS, "RSA");
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    private SigningKey generate(String publicKey, KeyAlgorithm keyAlgorithm){
        return SigningKey.builder()
                .publicKey(publicKey)
                .expiresAt(LocalDateTime.now().plusMinutes(KEY_VALIDITY_MINUTES))
                .keyAlgorithm(keyAlgorithm)
                .build();
    }

    @AllArgsConstructor
    @Getter
    private static class KeyPairHolder{
        private static PublicKey publicKey;
        private static PrivateKey privateKey;
    }
}
