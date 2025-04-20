package com.payme.common.constants;

public enum TokenType {
    ACCESS("ACCESS"),
    REFRESH("REFRESH");

    private final String tokenType;

    TokenType(String tokenType){
        this.tokenType = tokenType;
    }
}
