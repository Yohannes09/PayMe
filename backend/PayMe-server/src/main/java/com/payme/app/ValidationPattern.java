package com.payme.app;

import lombok.Getter;

@Getter
public enum ValidationPattern {
    USERNAME_PATTERN ("^[a-zA-Z0-9]{5,15}$"),
    EMAIL_PATTERN(""),
    PASSWORD_PATTERN ("^(?=.*[A-Z])(?=.*[0-9!@#$%^&*]).{6,}$"),
    USERNAME_VALIDATION_MESSAGE ("Username should be 5-15 characters long with no special symbols."),
    PASSWORD_VALIDATION_MESSAGE ("Password must be 6+ characters long, have at least 1 capital letter, and a number or special character."),
    EMAIL_VALIDATION_MESSAGE ("Entered invalid email");

    private final String pattern;

    ValidationPattern(String pattern){
        this.pattern = pattern;
    }

    public String getPattern(){
        return this.pattern;
    }
}
