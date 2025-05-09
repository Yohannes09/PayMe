package com.payme.authentication.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Getter
@AllArgsConstructor
public class TokenServiceClient {
    private final WebClient webClient;
    private final TokenServiceProperties tokenServiceProperties;



}
