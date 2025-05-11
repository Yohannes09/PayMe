package com.payme.authentication.components;

import com.payme.internal.security.dto.PublicKeyResponseDto;
import com.payme.internal.security.dto.TokenPairResponseDto;
import com.payme.internal.security.dto.UserTokenRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@AllArgsConstructor
public class TokenServiceClient {
    private final WebClient webClient;
    private final TokenServiceProperties tokenServiceProperties;


    public TokenPairResponseDto fetchAccessAndRefreshTokens(UserTokenRequestDto userTokenRequestDto){
        String accessAndRefreshEndpoint = tokenServiceProperties
                .getEndpoints()
                .getAccessAndRefreshToken();

        return webClient.post()
                .uri(accessAndRefreshEndpoint)
                .bodyValue(userTokenRequestDto)
                .retrieve()
                .onStatus(
                        httpStatusCode ->
                                httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),

                        clientResponse ->
                                clientResponse
                                        .bodyToMono(String.class)
                                        .map(body -> new RuntimeException(body + "."))
                ).bodyToMono(TokenPairResponseDto.class)
                .block();
    }

}
