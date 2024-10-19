package com.payme.app.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Currency {
    USD("USA"),
    MXN("MEXICO"),
    CAD("CANADA"),
    RUB("RUSSIA");

    private final String currencyLocation;
}
