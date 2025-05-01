package com.payme.token_provider.component;

import com.payme.common.constants.TokenRecipient;
import com.payme.common.constants.TokenType;
import com.payme.token_provider.entity.PublicKeyRecord;
import com.payme.token_provider.model.TokenSubject;
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
public class SignedTokenProvider {
    private final SigningKeyManager signingKeyManager;
    private final TokenProperties tokenProperties;


    public String generateToken(
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

    // issuedAtDelay: delay in issuing token
    private String buildToken(
            TokenSubject tokenSubject,
            Map<String, Object> claims,
            int validityInMinutes,
            int issuedAtDelay
    ){
        Instant now = Instant.now().plus(Duration.ofMinutes(issuedAtDelay));
        Instant expiration = now.plus(Duration.ofMinutes(validityInMinutes));

        PrivateKey privateKey = signingKeyManager.getActivePrivateKey();
        PublicKeyRecord publicKeyRecord = signingKeyManager.getActiveSigningKey();

        return Jwts.builder()
                .setHeaderParam("kid", publicKeyRecord.getId())
                .setClaims(claims)
                .setSubject(tokenSubject.getUsernameOrId())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(privateKey, publicKeyRecord.getSignatureAlgorithm())
                .compact();
    }

    private Map<String, Object> buildClaims(
            Set<String> roles,
            String tokenType,
            String tokenRecipient
    ){
        Map<String, Object> claims = new HashMap<>();

        claims.put("iss", tokenProperties.getIssuer()); // TokenProv should also share its iss
        claims.put("aud", "all-services"); // haven't thought out
        claims.put("rol", roles);
        // type: ACCESS, REFRESH, BOOTSTRAP
        claims.put("typ", tokenType);
        // recipient: USER, SERVICE
        claims.put("rec", tokenRecipient);

        return claims;
    }

}
