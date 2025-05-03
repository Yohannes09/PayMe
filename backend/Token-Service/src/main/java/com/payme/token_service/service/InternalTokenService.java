package com.payme.token_service.service;

import com.payme.internal.constant.PaymeRoles;
import com.payme.internal.constant.TokenRecipient;
import com.payme.internal.constant.TokenType;
import com.payme.token_service.component.token.TokenProvider;
import com.payme.token_service.component.token.properties.ServiceTokenProperties;
import com.payme.token_service.dto.TokenPairDto;
import com.payme.token_service.model.ServiceTokenSubject;
import com.payme.token_service.model.TokenSubject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Set;

/**
 *  Generates token for Microservices. ServiceTokenService unfortunately couldn't work as a name.*/
@Service
@Slf4j
@RequiredArgsConstructor
public class InternalTokenService {
    private static final String INITIALIZATION_SUBJECT = "INITIALIZATION_SUBJECT";
    private static final int INITIALIZATION_TOKEN_DELAY_MS = 1000 * 7; // 7s

    private final TokenProvider tokenProvider;
    private final ServiceTokenProperties serviceTokenProperties;

    /**
     * Issues a pair of tokens: an access token and refresh token, both valid immediately.
     */
    public TokenPairDto issueAccessAndRefresh(String serviceNameOrId){
        int accessTokenValidityMins = serviceTokenProperties.getAccessToken().getValidityMins();
        int refreshTokenValidityMins = serviceTokenProperties.getRefreshToken().getValidityMins();

        log.info("Issued token pair for {}", serviceNameOrId);
        return tokenProvider.issueAccessAndRefreshTokens(
                buildTokenSubject(serviceNameOrId),
                TokenRecipient.SERVICE.name(),
                accessTokenValidityMins,
                refreshTokenValidityMins
        );

    }

    /**
     * Generates a new access token with no issuance delay.
     */
    public String issueAccessToken(String serviceNameOrId){
        int tokenValidityMins = serviceTokenProperties.getAccessToken().getValidityMins();
        int issueAtDelayMins = serviceTokenProperties.getAccessToken().getIssueAtDelayMins();

        log.info("Issued access token for {}", serviceNameOrId);
        return tokenProvider.issueCustomToken(
                buildTokenSubject(serviceNameOrId),
                TokenType.ACCESS.name(),
                TokenRecipient.SERVICE.name(),
                tokenValidityMins,
                issueAtDelayMins
        );

    }


    /**
     * The delay allows the signing key to be created
     * before generating the token.
     */
    @Scheduled(initialDelay = INITIALIZATION_TOKEN_DELAY_MS)
    private void displayInitializationToken(){
        System.out.println("\nInitialization token: ");
        System.out.println(issueInitializationToken() + "\n");
    }

    /**
     * Issues an initialization token used to authenticate services during startup or initial handshake.
     */
    private String issueInitializationToken(){
        int tokenValidityMins = serviceTokenProperties.getInitializationToken().getValidityMins();
        int issuedAtDelayMins = serviceTokenProperties.getInitializationToken().getIssueAtDelayMins();

        return tokenProvider.issueCustomToken(
                buildTokenSubject(INITIALIZATION_SUBJECT),
                TokenType.INITIALIZATION.name(),
                TokenRecipient.SERVICE.name(),
                tokenValidityMins,
                issuedAtDelayMins
        );

    }

    private TokenSubject buildTokenSubject(String serviceNameOrId){
        return new ServiceTokenSubject(
                serviceNameOrId,
                Set.of(PaymeRoles.SERVICE.name())
        );
    }

}
