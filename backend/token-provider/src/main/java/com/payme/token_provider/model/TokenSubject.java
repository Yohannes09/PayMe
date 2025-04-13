package com.payme.token_provider.model;

import java.util.Map;

public interface TokenSubject {
    String getUsernameOrId();
    Map<String, Object> getClaims();
}
