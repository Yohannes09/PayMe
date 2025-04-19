package com.payme.common.util;

import com.payme.common.constants.ClaimsType;
import com.payme.common.constants.DomainConstants;
import com.payme.common.constants.PaymeRoles;
import com.payme.common.constants.TokenType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.stream.Collectors;

import static io.jsonwebtoken.Jwts.claims;

@RequiredArgsConstructor
public class ServiceTokenValidator implements TokenValidator{
    private final String issuer;// = DomainConstants.DEFAULT_ISSUER;
    // the token's audience should be the same as the recieving microservice's name
    private final String audience;
    private final Set<TokenType> requiredTokenTypes;
    private final Set<ClaimsType> requiredClaimTypes;
    private final Set<PaymeRoles> requiredRoles;


    @Override
    public boolean isTokenValid(
            String token,
            String signingKey,
            String signingAlgorithm
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {

        return hasValidType(token, signingKey, requiredClaimTypes, signingAlgorithm) &&
                hasValidRoles(token, signingKey, requiredRoles, signingAlgorithm) &&
                hasValidAudience(token, signingKey, signingAlgorithm) &&
                hasValidSubject(token, signingKey, signingAlgorithm);
    }

    private boolean hasValidType(
            String token,
            String signingKey,
            Set<ClaimsType> allowedTypes,
            String signingAlgorithm
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {

        Optional<String> extractedType = TokenResolver.resolveClaim(
                token,
                signingKey,
                signingAlgorithm,
                claims -> claims.get("type", String.class)
        );

        return allowedTypes.stream()
                .map(ClaimsType::getClaimsType)
                .anyMatch(extractedType::equals);
    }

    public boolean hasValidRoles(
            String token,
            String signingKey,
            Set<PaymeRoles> requiredRoles,
            String signingAlgorithm
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {

        var extractedRoles = TokenResolver.resolveClaim(
                token,
                signingKey,
                signingAlgorithm,
                claims -> claims.get("roles", List.class)
        );

        if(extractedRoles.isEmpty()){
            return false;
        }

        Set<String> reqRoles = requiredRoles.stream()
                .map(PaymeRoles::getRole)
                .collect(Collectors.toSet());

        return extractedRoles.get()
                .stream()
                .anyMatch(role -> reqRoles.contains(role));
    }

    private boolean hasValidSubject(
            String token,
            String signingKey,
            String signingAlgorithm
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {

        Optional<String> extractedSubject = TokenResolver.resolveClaim(
                token,
                signingKey,
                signingAlgorithm,
                Claims::getSubject
        );

        return extractedSubject.isPresent();
    }

    private boolean hasValidAudience(
            String token,
            String signingKey,
            String signingAlgorithm
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {

        Optional<String> extractedAudience = TokenResolver.resolveClaim(
                token,
                signingKey,
                signingAlgorithm,
                Claims::getAudience
        );

        return extractedAudience
                .map(audience::equals)
                .orElse(false);
    }

}

