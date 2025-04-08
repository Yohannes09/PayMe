package com.payme.internal_authentication.constant;

public enum ServiceType {
    INTERNAL("INTERNAL"),
    EXTERNAL("EXTERNAL");

    private final String serviceType;
    ServiceType(String serviceType){
        this.serviceType = serviceType;
    }


    public String getServiceType() {
        return serviceType;
    }
}
