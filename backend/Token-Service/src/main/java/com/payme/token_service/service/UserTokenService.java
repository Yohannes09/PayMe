package com.payme.token_service.service;

import com.payme.internal.security.constant.TokenRecipient;
import com.payme.internal.security.constant.TokenType;
import com.payme.token_service.component.token.TokenProvider;
import com.payme.token_service.component.token.properties.UserTokenProperties;
import com.payme.internal.security.dto.TokenPairResponseDto;
import com.payme.internal.security.dto.UserTokenRequestDto;
import com.payme.internal.security.model.TokenSubject;
import com.payme.internal.security.model.UserTokenSubject;
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
    public TokenPairResponseDto issueAccessAndRefresh(@Valid UserTokenRequestDto userTokenRequestDto){
        TokenSubject tokenSubject = mapRequestToTokenSubject(userTokenRequestDto);

        int accessTokenValidityMins = userTokenProperties.getAccessToken().getValidityMins();
        int refreshTokenValidityMins = userTokenProperties.getRefreshToken().getValidityMins();

        log.info("Issued token pair for {}", tokenSubject.getUsernameOrId());
        return tokenProvider.issueAccessAndRefreshTokens(
                tokenSubject,
                TokenRecipient.USER.toString(),
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
    public String issueAccessToken(UserTokenRequestDto userTokenRequestDto){
        TokenSubject tokenSubject = mapRequestToTokenSubject(userTokenRequestDto);

        int accessTokenValidityMins = userTokenProperties.getAccessToken().getValidityMins();
        int accessTokenIssueAtDelayMins = userTokenProperties.getAccessToken().getIssueAtDelayMins();

        log.info("Issued access token for {}", tokenSubject.getUsernameOrId());
        return tokenProvider.issueCustomToken(
                tokenSubject,
                TokenType.ACCESS.name(),
                TokenRecipient.USER.toString(),
                accessTokenValidityMins,
                accessTokenIssueAtDelayMins
        );
    }


    private TokenSubject mapRequestToTokenSubject(UserTokenRequestDto userTokenRequestDto){
        return new UserTokenSubject(userTokenRequestDto.usernameOrId(), userTokenRequestDto.roles());
    }

}
