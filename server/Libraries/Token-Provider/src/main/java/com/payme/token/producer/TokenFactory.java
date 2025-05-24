package com.payme.token.producer;

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
 * Generates signed JWT tokens using the active signing key.
 * <p>Encapsulates token creation logic to decouple from token services, ensuring modularity and maintainability.</p>
 */
@Component
@RequiredArgsConstructor
public class TokenFactory {
    private final SigningKeyManager signingKeyManager;
    private final TokenConfigurationProperties tokenConfigurationProperties;


    /**
     * Generates a new JWT token with specified parameters.
     *
     * @param tokenSubject       the subject (user or entity) for the token
     * @param tokenType         the type of token (e.g., access, refresh)
     * @param tokenRecipient    the intended recipient of the token
     * @param tokenValidityMinutes the token's validity duration in minutes
     * @return a signed JWT token as a string
     */
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


    /**
     * Builds a JWT token with the provided claims and validity period.
     *
     * @param tokenSubject      the subject for the token
     * @param claims           the claims to include in the token
     * @param validityMinutes  the token's validity duration in minutes
     * @return a signed JWT token as a string
     */
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


    /**
     * Creates a map of claims for the JWT token.
     *
     * @param roles         the roles or scopes for the token subject
     * @param tokenType     the type of token
     * @param tokenRecipient the intended recipient of the token
     * @return a map containing the token claims
     */
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


    /**
     * Resolves the signature algorithm from the provided signing algorithm name.
     *
     * @param signingAlgorithm the name of the signing algorithm
     * @return the corresponding {@link SignatureAlgorithm}
     */
    private SignatureAlgorithm signatureAlgorithmResolver(String signingAlgorithm){
        return SignatureAlgorithm.forName(signingAlgorithm);
    }

}
