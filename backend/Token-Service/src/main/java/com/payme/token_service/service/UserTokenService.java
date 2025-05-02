package com.payme.token_service.service;

import com.payme.common.constants.TokenRecipient;
import com.payme.common.constants.TokenType;
import com.payme.token_service.component.token.TokenProvider;
import com.payme.token_service.component.token.properties.TokenProperties;
import com.payme.token_service.component.token.properties.UserTokenProperties;
import com.payme.token_service.dto.TokenPairDto;
import com.payme.token_service.model.TokenSubject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service responsible for issuing JWT access and refresh tokens for users.
 *
 * <p>This service delegates signing logic to {@link TokenProvider} and applies
 * specific issuance strategies for different token types.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserTokenService {
    private final TokenProvider tokenProvider;
    private final UserTokenProperties userTokenProperties;

    /**
     * Issues a pair of tokens: an access token and refresh token, both valid immediately.
     */
    public TokenPairDto issueAccessAndRefresh(@Valid TokenSubject tokenSubject){
        int accessTokenValidityMins = userTokenProperties.getAccessToken().getValidityMins();
        int refreshTokenValidityMins = userTokenProperties.getRefreshToken().getValidityMins();

        log.info("Issued token pair for {}", tokenSubject.getUsernameOrId());
        return tokenProvider.issueAccessAndRefreshTokens(
                tokenSubject,
                TokenRecipient.USER.name(),
                accessTokenValidityMins,
                refreshTokenValidityMins
        );
    }

    /**
     * Generates a new access token with a short issuance delay.
     *
     * <p>This is prepared slightly in advance to reduce client latency and avoid overlapping
     * refresh calls, while still maximizing token lifetime.</p>
     */
    public String issueAccessToken(@Valid TokenSubject tokenSubject){
        int accessTokenValidityMins = userTokenProperties.getAccessToken().getValidityMins();
        int accessTokenIssueAtDelayMins = userTokenProperties.getAccessToken().getIssueAtDelayMins();

        log.info("Issued access token for {}", tokenSubject.getUsernameOrId());
        return tokenProvider.issueAccessToken(
                tokenSubject,
                TokenRecipient.USER.name(),
                accessTokenValidityMins,
                accessTokenIssueAtDelayMins
        );
    }

}
