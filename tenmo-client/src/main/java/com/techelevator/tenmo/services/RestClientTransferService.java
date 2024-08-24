package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.model.ClientTransfer;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Optional;

public class RestClientTransferService {
    private static final String ENDPOINT = "http://localhost:8080/api/tenmo/transfer/";

    private final RestTemplate restTemplate;

    private final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2bGFkaW1pciIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE3MjQ2MTgzMDR9.Q-ZYva5uyGiYJzV7z_FiHepIlWW-1TN6TJGuYx8-1ZXh-vd5wZr5Ugo0EWAohF4dyMuL4CEUbj4eMyryUOVBHA";

    public RestClientTransferService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestClientTransferService(){
        this.restTemplate = new RestTemplate();
    }

    public Optional<TransferDto> getTransferById(int transferId){
        StringBuilder url = new StringBuilder(ENDPOINT);
        url.append(transferId);

        System.out.println(url);
        try {
            HttpEntity<Void> transferEntity = new HttpEntity<>(getHeaders());

            ResponseEntity<TransferDto> response= restTemplate.exchange(
                    url.toString(),
                    HttpMethod.GET,
                    transferEntity,
                    TransferDto.class);

            return Optional.ofNullable(
                    response.getBody()
            );

        } catch (RestClientException e) {
            System.out.println("\nError connecting to service. " + e.getMessage());
        }catch (Exception e){
            System.out.println("\nError: " + e.getMessage());
        }
        return Optional.empty();
    }

    public HttpHeaders getHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return httpHeaders;
    }

    public static void main(String[] args) {
        System.out.println(new RestClientTransferService().getTransferById(3001).get().getAmount());
    }

}
