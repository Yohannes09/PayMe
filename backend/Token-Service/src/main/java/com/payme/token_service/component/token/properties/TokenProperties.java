package com.payme.token_service.component.token.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Base configuration class for token settings shared across all recipients.
 * Loads values from the 'token' prefix in application.yml.
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class TokenProperties {
    private String issuer;
}
