package com.payme.token_service.component.token.properties;

import com.payme.token_service.component.token.properties.models.TokenTiminigProperties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Loads access and refresh token settings for service recipients from
 * 'token.recipients.service' in application.yml.
 */
@Component
@Getter
@ConfigurationProperties(prefix = "token.recipients.service")
public class ServiceTokenProperties extends TokenProperties{
    private TokenTiminigProperties accessToken;
    private TokenTiminigProperties refreshToken;
}
