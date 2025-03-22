package com.payme.app.constants;

public enum CardType {
    VISA("VISA"),
    MASTER_CARD("MASTER_CARD");

    private final String cardType;

    CardType(final String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }
}
