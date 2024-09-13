package com.techelevator.tenmo.exception;

public class TransferException extends RuntimeException{
    public TransferException(){
        super();
    }

    public TransferException(String message){
        super(message);
    }

    public TransferException(String message, Exception cause){
        super(message, cause);
    }
}
