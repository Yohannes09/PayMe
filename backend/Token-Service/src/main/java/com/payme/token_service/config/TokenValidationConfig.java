package com.payme.token_service.config;

import com.payme.internal.security.constant.PaymeRoles;
import com.payme.internal.security.constant.TokenRecipient;
import com.payme.internal.security.constant.TokenType;
import com.payme.internal.security.token.ServiceTokenValidator;
import com.payme.token_service.component.token.properties.SharedTokenProperties;
import com.payme.token_service.util.data_structure.PublicKeyHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class TokenValidationConfig {
    private final SharedTokenProperties sharedTokenProperties;

    @Bean
    public ServiceTokenValidator serviceTokenValidator(){
        return new ServiceTokenValidator(
                sharedTokenProperties.getIssuer(),
                "token-service",
                Set.of(TokenType.ACCESS),
                Set.of(TokenRecipient.SERVICE, TokenRecipient.USER),
                Set.of(PaymeRoles.SERVICE, PaymeRoles.USER, PaymeRoles.ADMIN, PaymeRoles.SUPER_ADMIN)
        );
    }

    @Bean
    public PublicKeyHistory publicKeyHistory(){
        int size = 3;
        return new PublicKeyHistory(size);
    }

}
