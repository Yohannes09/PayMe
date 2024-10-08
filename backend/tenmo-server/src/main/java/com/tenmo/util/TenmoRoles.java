package com.tenmo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum TenmoRoles {
    USER("USER"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER ADMIN"),
    GUEST("GUEST");

    private static final Set<String> roles = Stream.of(TenmoRoles.values())
            .map(TenmoRoles::getRole)
            .collect(Collectors.toSet());

    private String role;

    public static Set<String> getRoles(){return roles;}

    public static boolean contains(String role){return roles.contains(role);}
}
