package com.payme.token_provider.config;

import com.payme.common.util.ServiceTokenValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenValidatorConfig {
    @Bean
    public ServiceTokenValidator serviceTokenValidator(){
        return new ServiceTokenValidator();
    }
}
