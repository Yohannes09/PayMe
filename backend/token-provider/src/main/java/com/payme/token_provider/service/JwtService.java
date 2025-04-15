package com.payme.token_provider.service;

import com.payme.token_provider.model.TokenSubject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

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

}
