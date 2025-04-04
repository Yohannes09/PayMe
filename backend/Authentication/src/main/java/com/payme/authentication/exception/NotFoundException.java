package com.payme.authentication.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    private static final Logger logger = LoggerFactory.getLogger(NotFoundException.class);

    public NotFoundException(String message){
        super(message);
        logger.error(message);

    }
    public NotFoundException(String message, Throwable cause){
        super(message, cause);
        logger.error(message);
    }
}
