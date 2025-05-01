package com.payme.token_provider.exception;

public class KeyInitializationException extends RuntimeException {
    public KeyInitializationException(String message) {
        super(message);
    }

    public KeyInitializationException(){
        super("Failed to initialize signing key. ");
    }
}
