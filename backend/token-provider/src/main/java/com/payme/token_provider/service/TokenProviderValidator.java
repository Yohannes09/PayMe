package com.payme.token_provider.service;


import com.payme.common.util.TokenValidator;

public class TokenProviderValidator extends TokenValidator {
    public TokenProviderValidator(String secret) {
        super(secret);
    }
}
