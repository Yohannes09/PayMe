package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.dto.TransferHistoryDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

/**
 * {@code TransferClientService} is responsible for sending requests
 * to the server API. It manages the communication with the API to retrieve
 * and transmit data as needed by the application.
 */

public class RestClientTransferService implements ClientTransferService{
    private static final String ENDPOINT = "http://localhost:8080/api/tenmo/transfer/";

    private final RestTemplate restTemplate;

    private String token ;

    public RestClientTransferService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestClientTransferService(){
        this.restTemplate = new RestTemplate();
    }

    public Optional<TransferDto> getTransferById(int transferId){
        StringBuilder url = new StringBuilder(ENDPOINT);
        url.append(transferId);

        try {

            ResponseEntity<TransferDto> response= restTemplate.exchange(
                    url.toString(),
                    HttpMethod.GET,
                    getEntityWithBearer(),
                    TransferDto.class);

            return Optional.ofNullable(response.getBody());

        } catch (RestClientException e) {
            System.out.println("\nError connecting to service. " + e.getMessage());
        }catch (Exception e){
            System.out.println("\nError: " + e.getMessage());
        }
        return Optional.empty();
    }


    @Override
    public List<TransferHistoryDto> accountTransferHistory(int accountId) {

        String url = String.format("%s/history/%d", ENDPOINT, accountId);
        //http://localhost:8080/api/tenmo/transfer/transfer-status/2001/1

        try {
            ResponseEntity<List<TransferHistoryDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    getEntityWithBearer(),
                    new ParameterizedTypeReference<List<TransferHistoryDto>>() {}
            );

            return response.getBody();

        }catch (RestClientException restClientException){
            System.out.println("Error: " + restClientException.getMessage());
            return List.of();
        }
    }

    // no idea what a 'ParameterizedTypeReference' is.
    // https://stackoverflow.com/questions/63281734/how-to-use-resttemplate-to-get-result-in-list-and-put-the-response-into-list

    public List<TransferDto> getTransfersByStatusId(int accountId, int transferStatusId){

        String url = String.format("%s/transfer-status/%d/%d", ENDPOINT, accountId, transferStatusId);
        //http://localhost:8080/api/tenmo/transfer/transfer-status/2001/1

        try {
            ResponseEntity<List<TransferDto>> response= restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    getEntityWithBearer(),
                    new ParameterizedTypeReference<List<TransferDto>>() {});

            return response.getBody();

        }catch (RestClientException restClientException){
            System.out.println("Error: " + restClientException.getMessage());
            return List.of();
        }
    }


    public void setToken(String token){
        this.token = token;
    }

    public HttpEntity getEntityWithBearer(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return new HttpEntity(httpHeaders);
    }

    public static void main(String[] args) {
        RestClientTransferService tester = new RestClientTransferService();
        tester.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2bGFkaW1pciIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE3MjQ5MjI0Mjd9.nZbI0mNIeJIDMQbzQBjiWl_9lDHLnPLHb8Xbt6jgM2H3QgK-eWVwe1xNlUQuW96z54qYvAG1zEWs1O7YU7RR4A");
        //System.out.println(new RestClientTransferService().getTransferById(3001).get().getAmount());
        tester.accountTransferHistory(2001).forEach(transferResponseDto -> System.out.println(transferResponseDto.toString()));
    }

}
