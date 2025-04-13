package com.payme.token_provider.model;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ServiceTokenSubject implements TokenSubject{
    private final String serviceName;
    private final Map<String, Object> claims;

    @Override
    public String getUsernameOrId() {
        return serviceName;
    }

    @Override
    public Map<String, Object> getClaims() {
        return Map.of(
                "roles", List.of("SERVICE"),
                "type", "SERVICE"
        );
    }
}
