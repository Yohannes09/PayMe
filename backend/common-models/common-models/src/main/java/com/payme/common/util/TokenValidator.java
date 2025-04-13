package com.payme.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class TokenValidator {
    private final String secret;


    public abstract void idk();
    public boolean isTokenValid(String jwtToken, String recipientUsernameOrId) {
        final String extractedUsername = extractClaim(jwtToken, Claims::getSubject);
        return recipientUsernameOrId.equals(extractedUsername) &&
                !isTokenExpired(jwtToken);
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimExtractor
    ){
        final Claims claims = extractAllClaims(token);
        return claimExtractor.apply(claims);
    }


    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
