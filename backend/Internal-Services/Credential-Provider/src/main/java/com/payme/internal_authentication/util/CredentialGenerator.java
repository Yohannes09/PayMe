package com.payme.internal_authentication.util;

import java.security.SecureRandom;
import java.util.Base64;

public class CredentialGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateCredential(int length){
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public static void main(String[] args) {
        System.out.println(generateCredential(32));
    }

}
