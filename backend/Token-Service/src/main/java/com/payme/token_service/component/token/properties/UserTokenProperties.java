package com.payme.token_service.component.token.properties;

import com.payme.token_service.component.token.properties.models.TokenTiminigProperties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Loads access and refresh token settings for user recipients from
 * 'token.recipients.user' in application.yml.
 */
@Component
@Getter
@ConfigurationProperties(prefix = "token.recipients.user")
public class UserTokenProperties extends TokenProperties {
    private TokenTiminigProperties accessToken;
    private TokenTiminigProperties refreshToken;
}
