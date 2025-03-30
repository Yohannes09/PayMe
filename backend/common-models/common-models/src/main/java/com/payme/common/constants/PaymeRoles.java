package com.payme.common.constants;

public enum PaymeRoles {
    USER("USER"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER ADMIN"),
    GUEST("GUEST");

    private final String role;

    PaymeRoles(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }
}
