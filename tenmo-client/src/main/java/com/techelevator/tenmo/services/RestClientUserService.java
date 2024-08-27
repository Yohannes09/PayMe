package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class RestClientUserService implements ClientUserService{
    private static final String ENDPOINT = "http://localhost:8080:api/tenmo/user";

    private final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2bGFkaW1pciIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE3MjQ4MzIwMDR9.W6_rtLWs0VpIuu33bc2hpYLJaZXoKUOjGFz1tikdyNeSIUpqI5LUzUAEswcOPijt2Hm5xdZR6jTB5WP2VJSn9A";

    private final RestTemplate restTemplate;

    public RestClientUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestClientUserService() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Optional<User> getUserById(int userId) {
        String url = String.format("%s/%d", ENDPOINT, userId);

        try{
            return Optional.ofNullable(restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    getEntityWithBearer(),
                    User.class
            ).getBody());

        }catch (RestClientException clientException){
            System.out.println("Error: " + clientException.getMessage());
            return Optional.empty();
        }
    }

    public HttpEntity getEntityWithBearer(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return new HttpEntity(httpHeaders);
    }
}
