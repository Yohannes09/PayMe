package com.payme.authentication.not_used;

public class TransactionException extends RuntimeException{
    public TransactionException(){
        super();
    }

    public TransactionException(String message){
        super(message);
    }

    public TransactionException(String message, Exception cause){
        super(message, cause);
    }
}
