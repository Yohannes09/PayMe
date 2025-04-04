package com.payme.authentication.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtConfig {
    @Value("${application.security.jwt.secret}")
    private String secret;

    @Value("${application.security.jwt.validity}")
    private Long validity;
}
