package com.payme.token_service.model;

import java.util.Set;

public interface TokenSubject {
    String getUsernameOrId();
    Set<String> getRolesOrScope();
}
