package com.payme.token_service.service;

import com.payme.common.constants.TokenRecipient;
import com.payme.token_service.component.token.TokenProvider;
import com.payme.token_service.component.token.properties.ServiceTokenProperties;
import com.payme.token_service.dto.TokenPairDto;
import com.payme.token_service.model.TokenSubject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternalTokenService {
    private final TokenProvider tokenProvider;
    private final ServiceTokenProperties serviceTokenProperties;

    /**
     * Issues a pair of tokens: an access token and refresh token, both valid immediately.
     */
    public TokenPairDto issueAccessAndRefresh(@Valid TokenSubject tokenSubject){
        int accessTokenValidityMins = serviceTokenProperties.getAccessToken().getValidityMins();
        int refreshTokenValidityMins = serviceTokenProperties.getRefreshToken().getValidityMins();

        log.info("Issued token pair for {}", tokenSubject.getUsernameOrId());
        return tokenProvider.issueAccessAndRefreshTokens(
                tokenSubject,
                TokenRecipient.SERVICE.name(),
                accessTokenValidityMins,
                refreshTokenValidityMins
        );

    }

    /**
     * Generates a new access token with no issuance delay.
     */
    public String issueAccessToken(@Valid TokenSubject tokenSubject){
        int accessTokenValidityMins = serviceTokenProperties.getAccessToken().getValidityMins();
        int accessTokenIssueAtDelayMins = serviceTokenProperties.getAccessToken().getIssueAtDelayMins();

        log.info("Issued access token for {}", tokenSubject.getUsernameOrId());
        return tokenProvider.issueAccessToken(
                tokenSubject,
                TokenRecipient.SERVICE.name(),
                accessTokenValidityMins,
                accessTokenIssueAtDelayMins
        );
    }

    // maybe token provider should have a more flexible token option besided access and refresh token

    public String issueBootStrapToken(){
        return null;
    }

}
