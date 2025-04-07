package com.payme.authentication.exception;

public class DuplicateCredentialException extends RuntimeException{
    public DuplicateCredentialException(String message){
        super(message);
    }

}
