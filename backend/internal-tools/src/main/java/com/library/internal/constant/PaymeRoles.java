package com.library.internal.constant;

public enum PaymeRoles {
    GUEST("GUEST"),
    USER("USER"),
    SERVICE("SERVICE"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER ADMIN");

    private final String role;

    PaymeRoles(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }
}
