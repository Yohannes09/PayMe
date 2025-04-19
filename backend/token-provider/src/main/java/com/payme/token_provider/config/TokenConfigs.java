package com.payme.token_provider.config;

import com.payme.common.constants.ClaimsType;
import com.payme.common.constants.DomainConstants;
import com.payme.common.constants.PaymeRoles;
import com.payme.common.constants.TokenType;
import com.payme.common.util.ServiceTokenValidator;
import com.payme.token_provider.ds.PublicKeyHistory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class TokenConfigs {
    @Bean
    public ServiceTokenValidator serviceTokenValidator(){
        return new ServiceTokenValidator(
                DomainConstants.DEFAULT_ISSUER,
                "token-service",
                Set.of(TokenType.ACCESS),
                Set.of(ClaimsType.SERVICE, ClaimsType.USER),
                Set.of(PaymeRoles.SERVICE, PaymeRoles.USER, PaymeRoles.ADMIN, PaymeRoles.SUPER_ADMIN)
        );
    }

    @Bean
    public PublicKeyHistory publicKeyHistory(){
        int size = 3;
        return new PublicKeyHistory(size);
    }
}
