package com.payme.token_provider.service;

import com.payme.common.constants.TokenRecipient;
import com.payme.common.constants.TokenType;
import com.payme.token_provider.component.SignedTokenProvider;
import com.payme.token_provider.constants.TokenConstants;
import com.payme.token_provider.dto.AuthenticationTokensDto;
import com.payme.token_provider.model.TokenSubject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service responsible for issuing JWT access and refresh tokens for users.
 *
 * <p>This service delegates signing logic to {@link SignedTokenProvider} and applies
 * specific issuance strategies for different token types.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserTokenService {
    private final SignedTokenProvider signedTokenProvider;


    /**
     * Issues a pair of tokens: an access token and refresh token, both valid immediately.
     */
    public AuthenticationTokensDto issueAccessAndRefresh(@Valid TokenSubject tokenSubject){
        String accessToken = signedTokenProvider.generateToken(
                tokenSubject,
                TokenType.ACCESS.toString(),
                TokenRecipient.USER.name(),
                TokenConstants.TOKEN_VALIDITY_MIN,
                TokenConstants.TOKEN_ISSUE_AT_NO_DELAY
        );

        String refreshToken = signedTokenProvider.generateToken(
                tokenSubject,
                TokenType.REFRESH.name(),
                TokenRecipient.USER.name(),
                TokenConstants.TOKEN_VALIDITY_MIN,
                TokenConstants.TOKEN_ISSUE_AT_NO_DELAY
        );

        log.info("Issued token pair for {}", tokenSubject.getUsernameOrId());
        return new AuthenticationTokensDto(accessToken, refreshToken);
    }

    /**
     * Generates a new access token with a short issuance delay.
     *
     * <p>This is prepared slightly in advance to reduce client latency and avoid overlapping
     * refresh calls, while still maximizing token lifetime.</p>
     */
    public String issueAccessToken(@Valid TokenSubject tokenSubject){
        log.info("Issued access token for {}", tokenSubject.getUsernameOrId());
        return signedTokenProvider.generateToken(
                tokenSubject,
                TokenType.ACCESS.toString(),
                TokenRecipient.USER.name(),
                TokenConstants.TOKEN_VALIDITY_MIN,
                TokenConstants.TOKEN_ISSUE_AT_DELAY
        );
    }

}
