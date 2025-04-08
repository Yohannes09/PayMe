package com.payme.internal_authentication.service;

import com.payme.internal_authentication.entity.Client;
import org.springframework.stereotype.Service;
import com.payme.internal_authentication.constant.ServiceType;
import com.payme.internal_authentication.dto.AuthenticationRequestDto;
import com.payme.internal_authentication.dto.AuthenticationResponseDto;
import com.payme.internal_authentication.entity.Credential;
import com.payme.internal_authentication.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService {
    private final ClientRepository clientRepository;
    private final CredentialService credentialService;

    // Each time client starts, it sends its information.
    @Transactional
    public AuthenticationResponseDto handleInitialRequest(AuthenticationRequestDto requestDto){
        Client client = clientRepository
                .findClientByBaseUrl(requestDto.baseUrl())
                .orElseGet(() -> {
                    Client newClient = handleNewClient(requestDto);
                    log.info("New client base URL: {}", newClient.getBaseUrl());

                    return newClient;
                });

        Credential credential = credentialService.resolveCredential(client);

        client.getCredentials().add(credential);
        clientRepository.save(client);
        log.info("New API Key created for service: {}", client.getServiceName());

        return new AuthenticationResponseDto(credential.getCredential());
    }


    private Client handleNewClient(AuthenticationRequestDto requestDto){
        return generateNewClient(
                requestDto.clientName(),
                requestDto.baseUrl(),
                requestDto.endpoint(),
                requestDto.description(),
                ServiceType.INTERNAL
        );

    }

    private Client generateNewClient(
            String serviceName,
            String baseUrl,
            String endpoint,
            String description,
            ServiceType serviceType
    ){
        return Client.builder()
                .serviceName(serviceName)
                .baseUrl(baseUrl)
                .endpoint(endpoint)
                .description(Optional.ofNullable(description).orElse(""))
                .serviceType(serviceType)
                .credentials(new ArrayList<>())
                .build();

    }

}
