package com.payme.gateway.service;

import com.payme.gateway.dto.AuthenticationResponseDto;
import com.payme.gateway.dto.LoginDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/*  CLIENT SENDS DATA TO GATEWAY -> GATEWAY VALIDATES AND CLEANSES DATA ->
*   GATEWAY SENDS RECEIVED DATA TO BACKEND SERVER -> BACKEND SERVER RESPONDS ->
*   GATEWAY RETURN RESPONSE TO CLIENT.*/

@Service
public class ClientRequestService {
    private final RestClient restClient;

    public ClientRequestService(RestClient restClient){
        this.restClient = restClient;
    }

    public AuthenticationResponseDto submitLoginCredentials(LoginDto loginDto){

    }
}
