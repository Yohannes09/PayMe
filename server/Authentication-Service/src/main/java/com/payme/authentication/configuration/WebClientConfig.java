package com.payme.authentication.configuration;

import com.payme.authentication.components.TokenServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final TokenServiceProperties tokenServiceProperties;

    @Bean("tokenServiceWebClient")
    public WebClient tokenServiceClient(WebClient.Builder webclient){
        return WebClient.builder()
                .baseUrl(tokenServiceProperties.getBaseUrl())
                .build();
    }
}
