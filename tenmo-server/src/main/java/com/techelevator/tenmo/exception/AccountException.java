package com.techelevator.tenmo.exception;

public class AccountException extends Exception{
    public AccountException(String message){
        super(message);
    }
    public AccountException(String message, Exception cause){
        super(message, cause);
    }
}
