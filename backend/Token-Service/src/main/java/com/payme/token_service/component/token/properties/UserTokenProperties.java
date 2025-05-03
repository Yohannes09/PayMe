package com.payme.token_service.component.token.properties;

import com.payme.token_service.component.token.properties.models.TokenTimingProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Loads access and refresh token settings for user recipients from
 * 'token.recipients.user' in application.yml.
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "token.recipients.user")
public class UserTokenProperties {
    private TokenTimingProperties accessToken;
    private TokenTimingProperties refreshToken;
}
