package com.payme.app.constants;

public enum Currency {
    USD("USA"),
    MXN("MEXICO"),
    CAD("CANADA"),
    RUB("RUSSIA");

    private final String countryName;

    Currency(String countryName){
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }
}
