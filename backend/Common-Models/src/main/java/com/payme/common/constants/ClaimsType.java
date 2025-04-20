package com.payme.common.constants;

import lombok.Getter;

@Getter
public enum ClaimsType {
    SERVICE("SERVICE"),
    USER("USER");

    private final String claimsType;

    ClaimsType(String claimsType){
        this.claimsType = claimsType;
    }
}
