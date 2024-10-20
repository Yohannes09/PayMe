package com.payme.app.authentication.service;

import com.payme.app.authentication.configuration.JwtConfig;
import com.payme.app.authentication.entity.SessionToken;
import com.payme.app.repository.SessionTokenRepository;
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
    private final SessionTokenRepository tokenRepository;

    public JwtService(
            JwtConfig jwtConfig,
            SessionTokenRepository tokenRepository
    ) {
        this.jwtConfig = jwtConfig;
        this.tokenRepository = tokenRepository;
    }

//    public String extractUsername(String jwtToken) {
//        return extractClaim(jwtToken, Claims::getSubject);
//    }

    /*
     * Why fetch the token?
     *
     *  In order to invalidate a token, simply delete from
     *  Database.
     *
     *  Thus, if the token is missing
     *  this method returns false.
     * */

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String username = extractClaim(jwtToken, Claims::getSubject);
        boolean isTokenStored = tokenRepository.findByToken(jwtToken).isPresent();

        return userDetails.getUsername().equals(username) &&
                !isTokenExpired(jwtToken) &&
                isTokenStored;
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
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
