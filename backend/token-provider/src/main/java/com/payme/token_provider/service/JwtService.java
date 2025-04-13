package com.payme.token_provider.service;

import com.payme.token_provider.model.TokenSubject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final long EXPIRATION_IN_SECONDS = 3600 * 1000L;

    private final SigningKeyManager signingKeyManager;


    public String generateToken(TokenSubject tokenSubject){
        return Jwts.builder()
                .setClaims(tokenSubject.getClaims())
                .setSubject(tokenSubject.getUsernameOrId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_IN_SECONDS))
                .signWith(signingKeyManager.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean isTokenValid(String jwtToken, String usernameOrId) {
        final String extractedUsername = extractClaim(jwtToken, Claims::getSubject);
        return usernameOrId.equals(extractedUsername) &&
                !isTokenExpired(jwtToken);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimExtractor){
        final Claims claims = extractAllClaims(token);
        return claimExtractor.apply(claims);
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(signingKeyManager.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
