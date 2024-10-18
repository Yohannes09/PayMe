package com.payme.app.exception;

public class DuplicateCredentialException extends RuntimeException{
    public DuplicateCredentialException(String message){
        super(message);
    }

}
