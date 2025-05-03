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

@Service
@Slf4j
@RequiredArgsConstructor
public class InternalTokenService {
    private static final String INITIALIZATION_SUBJECT = "INITIALIZATION_SUBJECT";
    private static final int INITIALIZATION_TOKEN_DELAY_MS = 1000 * 10; // 10s

    private final TokenProvider tokenProvider;
    private final ServiceTokenProperties serviceTokenProperties;

    /**
     * Issues a pair of tokens: an access token and refresh token, both valid immediately.
     */
    public TokenPairDto issueAccessAndRefresh(String usernameOrId){
        int accessTokenValidityMins = serviceTokenProperties.getAccessToken().getValidityMins();
        int refreshTokenValidityMins = serviceTokenProperties.getRefreshToken().getValidityMins();

        log.info("Issued token pair for {}", usernameOrId);
        return tokenProvider.issueAccessAndRefreshTokens(
                buildTokenSubject(usernameOrId),
                TokenRecipient.SERVICE.name(),
                accessTokenValidityMins,
                refreshTokenValidityMins
        );

    }

    /**
     * Generates a new access token with no issuance delay.
     */
    public String issueAccessToken(String usernameOrId){
        int tokenValidityMins = serviceTokenProperties.getAccessToken().getValidityMins();
        int issueAtDelayMins = serviceTokenProperties.getAccessToken().getIssueAtDelayMins();

        log.info("Issued access token for {}", usernameOrId);
        return tokenProvider.issueCustomToken(
                buildTokenSubject(usernameOrId),
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
        System.out.println("********* Initialization token *********\n");
        System.out.println(issueInitializationToken());
        System.out.println("\n****************************************");
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

    private TokenSubject buildTokenSubject(String usernameOrId){
        return new ServiceTokenSubject(
                usernameOrId,
                Set.of(PaymeRoles.SERVICE.name())
        );
    }

}
