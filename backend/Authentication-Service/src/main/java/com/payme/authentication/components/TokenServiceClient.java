package com.payme.authentication.components;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Configuration
public class TokenServiceClient {
    private final WebClient webClient;
    private final TokenServiceProperties tokenServiceProperties;

}
