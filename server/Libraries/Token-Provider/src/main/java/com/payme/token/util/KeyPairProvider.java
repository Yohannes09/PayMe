package com.payme.token.util;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class KeyPairProvider {

    public static KeyPair generateKeyPair(int sizeInBits, String algorithmType) throws NoSuchAlgorithmException {
        log.info("Generating key pair - Algorithm: {} - Size: {} bits", algorithmType, sizeInBits);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithmType);
        keyPairGenerator.initialize(sizeInBits);

        return keyPairGenerator.generateKeyPair();
    }

}
