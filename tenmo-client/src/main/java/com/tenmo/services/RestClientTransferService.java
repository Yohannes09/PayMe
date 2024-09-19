package com.tenmo.services;

import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.dto.transfer.TransferResponseDto;
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

    @Override
    public Optional<TransferRequestDto> getTransferById(int transferId){
        StringBuilder url = new StringBuilder(ENDPOINT);
        url.append(transferId);

        try {

            ResponseEntity<TransferRequestDto> response= restTemplate.exchange(
                    url.toString(),
                    HttpMethod.GET,
                    getEntityWithBearer(),
                    TransferRequestDto.class);

            return Optional.ofNullable(response.getBody());

        } catch (RestClientException e) {
            System.out.println("\nError connecting to service. " + e.getMessage());
        }catch (Exception e){
            System.out.println("\nError: " + e.getMessage());
        }
        return Optional.empty();
    }


    @Override
    public List<TransferResponseDto> accountTransferHistory(int accountId) {

        String url = String.format("%s/history/%d", ENDPOINT, accountId);

        try {
            ResponseEntity<List<TransferResponseDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    getEntityWithBearer(),
                    new ParameterizedTypeReference<List<TransferResponseDto>>() {}
            );

            return response.getBody();

        }catch (RestClientException restClientException){
            System.out.println("Error: " + restClientException.getMessage());
            return List.of();
        }
    }

//    public List<TransferDto> getTransfersByStatusId(int accountId, int transferStatusId){
//
//        String url = String.format("%s/transfer-status/%d/%d", ENDPOINT, accountId, transferStatusId);
//
//        try {
//            ResponseEntity<List<TransferDto>> response= restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    getEntityWithBearer(),
//                    new ParameterizedTypeReference<List<TransferDto>>() {});
//
//            return response.getBody();
//
//        }catch (RestClientException restClientException){
//            System.out.println("Error: " + restClientException.getMessage());
//            return List.of();
//        }
//    }


    public void setToken(String token){
        this.token = token;
    }

    public HttpEntity getEntityWithBearer(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return new HttpEntity(httpHeaders);
    }

}
