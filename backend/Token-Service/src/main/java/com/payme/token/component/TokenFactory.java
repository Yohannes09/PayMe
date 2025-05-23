package com.payme.token.component;

import com.payme.internal.security.model.TokenSubject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;


/**
 * Responsible for generating signed JWT tokens using the active signing key.
 *
 * <p>Encapsulates token creation logic to avoid tight coupling with token services.</p>
 */
@Component
@RequiredArgsConstructor
public class TokenFactory {
    private final SigningKeyManager signingKeyManager;
    private final TokenConfigurationProperties tokenConfigurationProperties;


    public String generateNewToken(
            TokenSubject tokenSubject,
            String tokenType,
            String tokenRecipient,
            int tokenValidityMinutes
    ){
        Map<String,Object> claims = addClaims(
                tokenSubject.getRolesOrScope(),
                tokenType,
                tokenRecipient
        );

        return buildTokenWithClaims(tokenSubject, claims, tokenValidityMinutes);
    }

    private String buildTokenWithClaims(
            TokenSubject tokenSubject,
            Map<String, Object> claims,
            int validityMinutes
    ){
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(Duration.ofMinutes(validityMinutes));

        PrivateKey privateKey = signingKeyManager.getActiveSigningKey();

        SignatureAlgorithm signatureAlgorithm = signatureAlgorithmResolver(
                signingKeyManager.getActivePublicKey().getSignatureAlgorithm()
        );

        return Jwts.builder()
                .setHeaderParam("kid", tokenConfigurationProperties.getSigning().getKeyId())
                .setClaims(claims)
                .setSubject(tokenSubject.getUsernameOrId())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiresAt))
                .signWith(privateKey, signatureAlgorithm)
                .compact();

    }

    private Map<String, Object> addClaims(
            Set<String> roles,
            String tokenType,
            String tokenRecipient
    ){

        return Map.of(
                "iss", tokenConfigurationProperties.getDefaultClaims().getIssuer(),
                "aud", tokenConfigurationProperties.getDefaultClaims().getAudience(),
                "rol", roles,
                "typ", tokenType,
                "rec", tokenRecipient
        );

    }

    private SignatureAlgorithm signatureAlgorithmResolver(String signingAlgorithm){
        return SignatureAlgorithm.forName(signingAlgorithm);
    }

}
