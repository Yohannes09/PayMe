package com.payme.app.constants;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final Set<String> roles = Stream.of(PaymeRoles.values())
            .map(PaymeRoles::getRole)
            .collect(Collectors.toSet());

    public static Set<String> getRoles(){
        return roles;
    }

    public static boolean contains(String role){
        return roles.contains(role);
    }
}
