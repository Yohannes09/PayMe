package com.payme.authentication.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Getter
@Configuration
public class RestClientConfig {
    @Value("${external.user-service.base-url}")
    private String baseUrl;
    @Value("${external.user-service.endpoints.user}")
    private String userEndpoint;

    @Bean
    public RestClient restClient(){
        return RestClient.create();
    }
}
