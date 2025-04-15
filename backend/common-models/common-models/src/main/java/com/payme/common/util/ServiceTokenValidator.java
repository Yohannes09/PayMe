package com.payme.common.util;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Set;

public class ServiceTokenValidator extends TokenValidator{
    private static final String REQUIRED_CLAIMS_TYPE = "SERVICE";

    public boolean hasValidClaims(String token, String signingKey, Set<String> allowedRoles){
        String extractedType = extractClaim(
                token,
                signingKey,
                claims -> claims.get("type", String.class)
        );
        if(extractedType == null || !extractedType.equals(REQUIRED_CLAIMS_TYPE)){
            return false;
        }

        String extractedSubject = extractClaim(token, signingKey, Claims::getSubject);
        if(extractedSubject == null){
            return false;
        }

        List<String> extractedRoles = extractClaim(
                token,
                signingKey,
                claims -> claims.get("roles", List.class)
        );
        if(extractedRoles == null){
            return false;
        }

        return extractedRoles.stream().anyMatch(allowedRoles::contains);
    }

    public boolean isTokenValid(String token, String signingKey){
        return !isTokenExpired(token, signingKey);
    }

}
