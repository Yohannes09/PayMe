package com.payme.authentication.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for connecting to an external token service.
 *
 * <p>This class binds to the properties defined under the prefix
 * <strong>{@code external.token-service}</strong> in the application's configuration file
 * (e.g., {@code application.yml} or {@code application.properties}).</p>
 *
 * <p>It encapsulates the base URL of the token service and its various endpoint paths,
 * such as those for retrieving access tokens, refresh tokens, and public keys.</p>
 *
 * <p>Example configuration:
 * <pre>
 * external:
 *   token-service:
 *     base-url: https://api.example.com/token-service
 *     endpoints:
 *       access-and-refresh-token: /token
 *       access-token: /token/access
 *       refresh-token: /token/refresh
 *       public-key: /token/public-key
 * </pre>
 * </p>
 *
 */
@Component
@ConfigurationProperties(prefix = "external.token-service")
@Getter
@Setter
public class TokenServiceProperties {
    private String baseUrl;
    private Endpoints endpoints;

    @Getter
    @Setter
    public static class Endpoints{
        private String accessAndRefreshToken;
        private String accessToken;
        private String refreshToken;
        private String publicKey;
    }

}
