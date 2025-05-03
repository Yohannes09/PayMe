package com.payme.token_service.component.token;

import com.payme.internal.constant.TokenType;
import com.payme.token_service.component.signing_key.SigningKeyManager;
import com.payme.token_service.component.token.properties.TokenProperties;
import com.payme.token_service.dto.TokenPairDto;
import com.payme.token_service.entity.PublicKeyRecord;
import com.payme.token_service.model.TokenSubject;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Responsible for generating signed JWT tokens using the active signing key.
 *
 * <p>Encapsulates token creation logic to avoid tight coupling with token services.</p>
 */
@Component
@RequiredArgsConstructor
public class TokenProvider {
    private final SigningKeyManager signingKeyManager;
    private final TokenProperties tokenProperties;


    public TokenPairDto issueAccessAndRefreshTokens(
            TokenSubject tokenSubject,
            String tokenRecipient,
            int accessTokenValidityMins,
            int refreshTokenValidityMins
    ){
        int issuedAtDelayMins = 0;

        String accessToken = buildTokenWithClaims(
                tokenSubject,
                TokenType.ACCESS.name(),
                tokenRecipient,
                accessTokenValidityMins,
                issuedAtDelayMins
        );

        String refreshToken = buildTokenWithClaims(
                tokenSubject,
                TokenType.REFRESH.name(),
                tokenRecipient,
                refreshTokenValidityMins,
                issuedAtDelayMins
        );

        return new TokenPairDto(accessToken, refreshToken);
    }

    public String issueCustomToken(
            TokenSubject tokenSubject,
            String tokenType,
            String tokenRecipient,
            int accessTokenValidityMins,
            int issuedAtDelayMins
    ){
        return buildTokenWithClaims(
                tokenSubject,
                tokenType,
                tokenRecipient,
                accessTokenValidityMins,
                issuedAtDelayMins
        );

    }


    private String buildTokenWithClaims(
            TokenSubject tokenSubject,
            String tokenType,
            String tokenRecipient,
            int tokenValidityInMinutes,
            int issuedAtDelay
    ){
        Map<String,Object> claims = buildClaims(
                tokenSubject.getRolesOrScope(),
                tokenType,
                tokenRecipient
        );

        String token = buildToken(
                tokenSubject,
                claims,
                tokenValidityInMinutes,
                issuedAtDelay
        );

        return token;
    }

    // issuedAtDelayMins: delay in issuing token
    private String buildToken(
            TokenSubject tokenSubject,
            Map<String, Object> claims,
            int validityInMins,
            int issuedAtDelayMins
    ){
        Instant issuedAt = Instant.now().plus(Duration.ofMinutes(issuedAtDelayMins));
        Instant expiresAt = issuedAt.plus(Duration.ofMinutes(validityInMins));

        PrivateKey privateKey = signingKeyManager.getActivePrivateKey();
        PublicKeyRecord publicKeyRecord = signingKeyManager.getActiveSigningKey();

        return Jwts.builder()
                .setHeaderParam("kid", publicKeyRecord.getId())
                .setClaims(claims)
                .setSubject(tokenSubject.getUsernameOrId())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiresAt))
                .signWith(privateKey, publicKeyRecord.getSignatureAlgorithm())
                .compact();
    }

    private Map<String, Object> buildClaims(
            Set<String> roles,
            String tokenType,
            String tokenRecipient
    ){
        Map<String, Object> claims = new HashMap<>();

        claims.put("iss", tokenProperties.getIssuer());
        claims.put("aud", "all-services"); // haven't thought out
        claims.put("rol", roles);
        claims.put("typ", tokenType);   // type: ACCESS, REFRESH, BOOTSTRAP
        claims.put("rec", tokenRecipient);  // recipient: USER, SERVICE

        return claims;
    }

}
