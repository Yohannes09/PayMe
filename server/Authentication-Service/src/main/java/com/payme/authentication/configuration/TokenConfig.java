package com.payme.authentication.configuration;

import com.payme.token.management.TokenConfigurationProperties;
import com.payme.token.management.secured.SigningKeyManager;
import com.payme.token.model.PublicKeyRecord;
import com.payme.token.model.PublicKeyRecordJpa;
import com.payme.token.persistence.PublicKeyHistory;
import com.payme.token.persistence.PublicKeyHistoryImp;
import com.payme.token.persistence.PublicKeyStore;
import com.payme.token.persistence.PublicKeyStoreJpa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {

    @Bean
    public SigningKeyManager<PublicKeyRecordJpa> signingKeyManager(
            PublicKeyStore<PublicKeyRecordJpa> publicKeyStore,
            TokenConfigurationProperties tokenConfigurationProperties,
            PublicKeyHistory publicKeyHistoryImp,
            SigningKeyManager.KeyFactory<PublicKeyRecordJpa> publicKeyFactory
    ){
        return new SigningKeyManager<PublicKeyRecordJpa>(
                publicKeyStore,
                tokenConfigurationProperties,
                publicKeyHistoryImp,
                publicKeyFactory
        );

    }

    @Bean
    public PublicKeyStore<PublicKeyRecordJpa> publicKeyStore(PublicKeyStoreJpa publicKeyStoreJpa){
        return publicKeyStoreJpa;
    }

    public SigningKeyManager.KeyFactory<PublicKeyRecord> publicKeyFactory(){
        return (publicKey, algorithm, issuedAt, expiresAt) ->
                PublicKeyRecordJpa.builder()
                        .publicKey(publicKey)
                        .signatureAlgorithm(algorithm)
                        .createdAt(issuedAt)
                        .expiresAt(expiresAt)
                        .build();
    }

}
