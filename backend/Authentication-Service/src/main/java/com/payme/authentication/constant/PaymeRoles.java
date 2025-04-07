package com.payme.authentication.constant;

import java.util.Arrays;
import java.util.List;

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

    public static List<PaymeRoles> allRoles(){
        return Arrays.stream(PaymeRoles.values()).toList();
    }

}
