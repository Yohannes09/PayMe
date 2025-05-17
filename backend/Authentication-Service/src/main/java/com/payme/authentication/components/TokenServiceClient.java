package com.payme.authentication.components;

import com.payme.internal.security.dto.PublicKeyResponseDto;
import com.payme.internal.security.dto.TokenPairResponseDto;
import com.payme.internal.security.dto.UserTokenRequestDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
//@RequiredArgsConstructor
public class TokenServiceClient {
    private final WebClient webClient;
    private final TokenServiceProperties tokenServiceProperties;


    public TokenServiceClient(
            @Qualifier("tokenServiceWebClient") WebClient webClient,
            TokenServiceProperties tokenServiceProperties
    ){
        this.webClient = webClient;
        this.tokenServiceProperties = tokenServiceProperties;
    }
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
