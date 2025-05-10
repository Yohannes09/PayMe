package com.payme.authentication.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "external.accessToken-service")
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
    }

}
