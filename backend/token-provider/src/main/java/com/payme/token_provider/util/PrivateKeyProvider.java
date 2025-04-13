package com.payme.token_provider.util;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class PrivateKeyProvider {

    public static KeyPair generateKeyPair(int sizeInBits, String algorithmType) throws NoSuchAlgorithmException {
        log.info("Generating key pair: Algorithm: {} size:{}", algorithmType, sizeInBits);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithmType);
        keyPairGenerator.initialize(sizeInBits);

        return keyPairGenerator.generateKeyPair();
    }

}
