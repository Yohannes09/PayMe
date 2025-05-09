package com.payme.internal.security.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;
// Kinda regret having this here since mainly auth service uses this.
@Getter
@RequiredArgsConstructor
public enum PaymeRoles {
    GUEST("GUEST"),
    USER("USER"),
    SERVICE("SERVICE"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER ADMIN");

    private final String role;

    public static Set<PaymeRoles> getAll(){
        return PaymeRoles.getAll();
    }

}
