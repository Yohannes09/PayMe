package com.payme.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Setter;

import java.util.Date;
import java.util.function.Function;

@Setter
public abstract class TokenValidator {

    public <T> T extractClaim(
            String token,
            String signingKey,
            Function<Claims, T> claimExtractor
    ){
        final Claims claims = extractAllClaims(token, signingKey);
        return claimExtractor.apply(claims);
    }

    public boolean isTokenExpired(String token, String signingKey){
        Date expiration = extractClaim(token, signingKey, Claims::getExpiration);
        return expiration.before(new Date());
    }


    private Claims extractAllClaims(String token, String signingKey){
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
