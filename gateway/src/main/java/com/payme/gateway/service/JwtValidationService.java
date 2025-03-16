package com.payme.gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
public class JwtValidationService {
    private final RestClient restClient;

    @Value("${application.api.endpoints.authentication-service}")
    private String baseUrl;
    @Value("${application.security.gateway.api-key}")
    private String backendApiKey;
    private Key signingKey;


    public JwtValidationService(RestClient.Builder restClientBuilder){
        this.restClient = restClientBuilder.build();
    }


    @PostConstruct
    public void fetchJwtSecret(){
        try{
            ResponseEntity<String> response = restClient.get()
                    .uri(baseUrl+"/public-key")
                    .header("api-key", backendApiKey)
                    .retrieve()
                    .toEntity(String.class);

            this.signingKey = Keys.hmacShaKeyFor(response.getBody().getBytes());
            log.info("Jwt secret-key successfully retrieved.");
        }catch (NullPointerException pointerException){
            log.error("Error retrieving jwt Secret-key. ", pointerException);
            throw new IllegalStateException("Failed to initialize Secret key", pointerException);
        }
    }

    public boolean isTokenValid(String token, UserDetails user){
        return usernameMatchesSubject(token, user) && !isTokenExpired(token);
    }
    public boolean isTokenValid(String token, String username){
        return usernameMatchesSubject(token, username) && !isTokenExpired(token);
    }



    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claimsResolver.apply(claims);
    }

    private boolean usernameMatchesSubject(String token, UserDetails user){
        String extractedUsername = extractClaim(token, Claims::getSubject);
        return user.getUsername().equals(extractedUsername);
    }
    private boolean usernameMatchesSubject(String token, String username){
        return username.equals(extractClaim(token, Claims::getSubject));
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
