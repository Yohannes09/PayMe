package com.payme.authentication.service;

import com.payme.authentication.configuration.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    public JwtService(JwtConfig jwtConfig){
        this.jwtConfig = jwtConfig;
    }


    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String extractedUsername = extractClaim(jwtToken, Claims::getSubject);
        return userDetails.getUsername().equals(extractedUsername) &&
                !isTokenExpired(jwtToken);
    }

    public boolean isTokenValid(String jwtToken, String username) {
        final String extractedUsername = extractClaim(jwtToken, Claims::getSubject);
        return username.equals(extractedUsername) && !isTokenExpired(jwtToken);
    }


    public String generateToken(UserDetails userDetails){
        Map<String, Object> roleClaims = new HashMap<>();
        roleClaims.put("roles", userDetails.getAuthorities());
        return generateToken(roleClaims, userDetails);
    }


    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getValidity()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }


    private Key getSignInKey() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }
}
