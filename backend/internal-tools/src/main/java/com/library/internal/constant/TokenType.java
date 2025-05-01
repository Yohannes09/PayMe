package com.library.internal.constant;

public enum TokenType {
    ACCESS("ACCESS"),
    REFRESH("REFRESH"),
    BOOTSTRAP("BOOTSTRAP");

    private final String tokenType;

    TokenType(String tokenType){
        this.tokenType = tokenType;
    }
}
