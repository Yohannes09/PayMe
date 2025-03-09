package com.payme.gateway.service;

import com.payme.gateway.dto.AuthenticationResponseDto;
import com.payme.gateway.dto.LoginDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/*  CLIENT SENDS DATA TO GATEWAY -> GATEWAY VALIDATES AND CLEANSES DATA ->
*   GATEWAY SENDS RECEIVED DATA TO BACKEND SERVER -> BACKEND SERVER RESPONDS ->
*   GATEWAY RETURN RESPONSE TO CLIENT.*/

@Service
public class ClientRequestService {
    private final RestClient restClient;

    @Value("${application.api.endpoints.authentication-service}")
    private String baseUrl;

    public ClientRequestService(RestClient.Builder restClient){
        this.restClient = restClient.build();
    }

    public AuthenticationResponseDto submitLoginCredentials(LoginDto loginDto){
        try {
            ResponseEntity<AuthenticationResponseDto> response = restClient.get()
                    .uri(baseUrl+"/login")
                    .retrieve()
                    .toEntity(AuthenticationResponseDto.class);

        }catch (Exception e){

        }
        return null;
    }
}
