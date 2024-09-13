package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class RestClientAccountService implements ClientAccountService{
    private static final String ENDPOINT = "http://localhost:8080/api/tenmo/account";

    private final RestTemplate restTemplate;

    private String token;

    public RestClientAccountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestClientAccountService() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Optional<Account> getAccountById(int accountId) {
        String url = String.format("%s/%d" , ENDPOINT, accountId);

        try {
            return Optional.ofNullable(restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    getEntityWithBearer(),
                    Account.class
            ).getBody());

        }catch (RestClientException clientException){
            System.out.println("Error: " + clientException.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Account> getAccountByUserId(int userId) {
        String url = String.format("%s/user/%d" , ENDPOINT, userId);

        try {
            return Optional.ofNullable(restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    getEntityWithBearer(),
                    Account.class
            ).getBody());

        }catch (RestClientException clientException){
            System.out.println("Error: " + clientException.getMessage());
            return Optional.empty();
        }
    }

    public void setToken(String token){
        this.token = token;
    }

    private HttpEntity getEntityWithBearer(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return new HttpEntity(httpHeaders);
    }

}
