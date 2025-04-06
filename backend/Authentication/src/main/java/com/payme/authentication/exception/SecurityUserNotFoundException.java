package com.payme.authentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SecurityUserNotFoundException extends RuntimeException {
    public SecurityUserNotFoundException(String message) {
        super(message);
    }
}
