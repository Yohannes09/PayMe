package com.payme.token_service.component.token.properties;

import com.payme.token_service.component.token.properties.models.TokenTimingProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Loads access and refresh token settings for service recipients from
 * 'token.recipients.service' in application.yml.
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "token.recipients.service")
public class ServiceTokenProperties {
    private TokenTimingProperties accessToken;
    private TokenTimingProperties refreshToken;
    private TokenTimingProperties initializationToken;
}
