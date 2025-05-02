package com.payme.token_service.config;

import com.payme.common.constants.PaymeRoles;
import com.payme.common.constants.TokenRecipient;
import com.payme.common.constants.TokenType;
import com.payme.common.util.ServiceTokenValidator;
import com.payme.token_service.component.token.properties.TokenProperties;
import com.payme.token_service.data_structure.PublicKeyHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class TokenValidationConfig {
    private final TokenProperties tokenProperties;

    @Bean
    public ServiceTokenValidator serviceTokenValidator(){
        return new ServiceTokenValidator(
                tokenProperties.getIssuer(),
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
