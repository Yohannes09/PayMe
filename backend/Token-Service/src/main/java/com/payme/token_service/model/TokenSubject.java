package com.payme.token_provider.model;

import java.util.Set;

public interface TokenSubject {
    String getUsernameOrId();
    Set<String> getRolesOrScope();
}
