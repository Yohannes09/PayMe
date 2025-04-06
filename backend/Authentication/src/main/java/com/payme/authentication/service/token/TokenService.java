package com.payme.authentication.service.token;

import com.payme.authentication.dto.AuthenticationResponseDto;
import com.payme.authentication.entity.SecurityUser;
import com.payme.authentication.entity.Token;
import com.payme.authentication.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

// Manages Jwt tokens instead of burdening auth service
// eventually add token refresh. Needs redis caching as token will be accessed frequently
@Slf4j
@Service
public class TokenService {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    public TokenService(TokenRepository tokenRepository, JwtService jwtService) {
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
    }

    public AuthenticationResponseDto initializeUserSession(SecurityUser securityUser){
        UUID id = securityUser.getId();

        String token = createUserSession(securityUser);

        manageUserSessions(id);

        return generateAuthenticationResponse(id, token);
    }


    private AuthenticationResponseDto generateAuthenticationResponse(UUID id, String token){
        return AuthenticationResponseDto.builder()
                .userId(id)
                .token(token)
                .build();
    }

    private String createUserSession(SecurityUser securityUser){
        String token = jwtService.generateToken(securityUser);

        Date creationTime = jwtService.extractClaim(token, Claims::getIssuedAt);
        Date expirationTime = jwtService.extractClaim(token, Claims::getExpiration);

        Token sessionToken = Token.builder()
                .securityUser(securityUser)
                .token(token)
                .isValid(true)
                .createdAt(creationTime)
                .expiresAt(expirationTime)
                .build();

        tokenRepository.save(sessionToken);
        log.info("Created session for user: {}", securityUser.getUsername());

        return token;
    }

    private void manageUserSessions(UUID id){
        log.info("Managing user sessions ID: {} ", id);

        List<Token> userActiveSessions = tokenRepository
                .findAllByUserId(id)
                .stream()
                .filter(token -> {
                    return jwtService.isTokenExpired(token.getToken());
                })
                .toList();

        if(userActiveSessions.size() >= 5){
            userActiveSessions.stream()
                    .min(Comparator.comparing(Token::getCreatedAt))
                    .map(Token::getToken)
                    .ifPresent(tokenRepository::deleteByToken);
        }
    }

    public void deleteByToken(String token) {

    }
}
