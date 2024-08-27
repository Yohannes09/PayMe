package com.techelevator.tenmo.services;

import com.techelevator.tenmo.exception.AccountException;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class RestClientAccountService implements ClientAccountService{
    private static final String ENDPOINT = "http://localhost:8080/api/tenmo/account";

    private final RestTemplate restTemplate;

    private final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2bGFkaW1pciIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE3MjQ4MzIwMDR9.W6_rtLWs0VpIuu33bc2hpYLJaZXoKUOjGFz1tikdyNeSIUpqI5LUzUAEswcOPijt2Hm5xdZR6jTB5WP2VJSn9A";

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


    private HttpEntity getEntityWithBearer(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return new HttpEntity(httpHeaders);
    }

    public static void main(String[] args) {
        RestClientAccountService service = new RestClientAccountService();
        Optional<Account> account = service.getAccountByUserId(1001);

        System.out.println(account.get().getAccountId() + " " + account.get().getBalance() + " " + account.get().getUserId() + " " );
    }
}
