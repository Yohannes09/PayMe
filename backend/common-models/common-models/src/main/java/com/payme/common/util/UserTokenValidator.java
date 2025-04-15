package com.payme.common.util;

import io.jsonwebtoken.Claims;

public class UserTokenValidator extends TokenValidator{

    public boolean isTokenValid(String jwtToken, String signingKey, String usernameOrEmail) {
        final String extractedUsername = extractClaim(jwtToken, signingKey, Claims::getSubject);

        return usernameOrEmail.equals(extractedUsername) && !isTokenExpired(jwtToken, signingKey);
    }

}
