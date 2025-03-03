package com.payme.app.constants;

public enum AccountType {
    PERSONAL("PERSONAL"),
    BUSINESS("BUSINESS"),
    JOINT("JOINT");

    private final String accountType;

    AccountType(String accountType) {
        this.accountType = accountType;
    }

    private String getAccountType() {
        return accountType;
    }
}
