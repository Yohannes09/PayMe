package com.payme.gateway.service;

import com.payme.gateway.dto.AuthenticationResponseDto;
import com.payme.gateway.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.naming.AuthenticationException;
import java.util.Optional;

/*  CLIENT SENDS DATA TO GATEWAY -> GATEWAY VALIDATES TOKEN VALIDITY ->
*   GATEWAY SENDS RECEIVED DATA TO BACKEND SERVER -> BACKEND SERVER RESPONDS ->
*   GATEWAY RETURN RESPONSE TO CLIENT.*/
@Slf4j
@AllArgsConstructor
@Service
public class AuthenticationRequestService {
    private final RestClient restClient;
    private final JwtValidationService jwtValidationService;
    private final RedisTokenService redisTokenService;

    @Value("${application.api.endpoints.authentication-service}")
    private String baseUrl;


    public AuthenticationResponseDto submitLoginCredentials(LoginDto loginDto) throws AuthenticationException {
        try {
            ResponseEntity<AuthenticationResponseDto> response = restClient.post()
                    .uri(baseUrl+"/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(loginDto)
                    .retrieve()
                    .toEntity(AuthenticationResponseDto.class);

            var responseBody = response.getBody();
            if(responseBody == null){
                throw new IllegalStateException("Unable to fetch authentication response. ");
            }

            String retrievedUserId = responseBody.userId().toString();
            String retrievedToken = responseBody.token();
            redisTokenService.saveToken(retrievedUserId, retrievedToken);

            return responseBody;
        }catch (Exception e){
            log.error("An error occurred when authenticating a user: {}", e.getLocalizedMessage());
            throw new AuthenticationException("Something went wrong. " + e.getLocalizedMessage());
        }

    }

    // Useless for this service as it's assumed the user will not have a token
    // when logging in.
    private String extractTokenFromRequest(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(authHeader -> authHeader.startsWith("Bearer"))
                .map(auth -> auth.substring(7))
                .orElse(null);
    }

}
