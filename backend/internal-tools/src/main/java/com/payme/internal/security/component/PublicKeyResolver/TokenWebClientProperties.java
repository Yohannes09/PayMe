package com.payme.internal.security.component.PublicKeyResolver;

import java.util.Map;

public interface TokenWebClientProperties {
    String getBaseUrl();
    String getPublicKeyUri();
    Map<String, String>  getEndpoints();
}
