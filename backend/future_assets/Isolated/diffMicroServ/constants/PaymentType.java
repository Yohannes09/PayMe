package com.payme.authentication.diffMicroServ.constants;

public enum PaymentType {
    CREDIT_CARD("CREDIT_CARD"),
    DEBIT_CARD("DEBIT_CARD"),
    BANK_ACCOUNT("BANK_ACCOUNT");

    private final String paymentType;

    PaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentType() {
        return paymentType;
    }

}
