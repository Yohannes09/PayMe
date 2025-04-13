package com.payme.token_provider.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

public class UserTokenSubject implements TokenSubject {
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserTokenSubject(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    @Override
    public String getUsernameOrId() {
        return username;
    }

    @Override
    public Map<String, Object> getClaims() {
        return Map.of(
                "roles", authorities.stream().map(GrantedAuthority:: getAuthority).toList(),
                "type", "USER"
        );
    }
}
