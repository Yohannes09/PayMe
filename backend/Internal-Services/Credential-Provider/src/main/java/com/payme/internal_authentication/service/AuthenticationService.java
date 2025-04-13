package com.payme.internal_authentication.service;

import com.payme.internal_authentication.constant.ValidationPattern;
import com.payme.internal_authentication.dto.ClientRegisterDto;
import com.payme.internal_authentication.entity.Client;
import com.payme.internal_authentication.exception.ClientBadRequestException;
import com.payme.internal_authentication.exception.FailedRegisterException;
import com.payme.internal_authentication.util.CredentialGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.payme.internal_authentication.constant.ServiceType;
import com.payme.internal_authentication.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final int CREDENTIAL_SIZE = 32;

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String register(ClientRegisterDto clientRegisterDto){
        if(clientRepository.existsByNameOrBaseUrl(clientRegisterDto.name(), clientRegisterDto.baseUrl())){
            log.warn("Client failed to register, duplicate service name or base url provided: {}", clientRegisterDto.name());
            throw new FailedRegisterException("A service with the provided name or Base URL exists already. ");
        }

        String secret = CredentialGenerator.generateCredential(CREDENTIAL_SIZE);
        Client client = handleNewClient(clientRegisterDto, secret);
        clientRepository.save(client);

        return secret;
    }

    private Client handleNewClient(ClientRegisterDto registerDto, String secret){
        validateClient(registerDto);
        return generateNewClient(
                registerDto.name(),
                secret,
                registerDto.baseUrl(),
                registerDto.description(),
                ServiceType.INTERNAL
        );
    }

    private void validateClient(ClientRegisterDto registerDto){
        if(!registerDto.name().matches(ValidationPattern.CLIENT_NAME_PATTERN)){
            throw new ClientBadRequestException(ValidationPattern.CLIENT_NAME_MESSAGE);
        }

        if(registerDto.baseUrl().matches(ValidationPattern.BASE_URL_PATTERN)){
            throw new ClientBadRequestException(ValidationPattern.BASE_URL_MESSAGE);
        }
    }

    private Client generateNewClient(
            String name,
            String secret,
            String baseUrl,
            String description,
            ServiceType serviceType
    ){
        return Client.builder()
                .name(name)
                .secret(passwordEncoder.encode(secret))
                .baseUrl(baseUrl)
                .description(Optional.ofNullable(description).orElse(""))
                .serviceType(serviceType)
                .endpoints(new ArrayList<>())
                .active(true)
                .build();
    }

}
//    private final CredentialService credentialService;
//
//    // Each time client starts, it sends its information.
//    @Transactional
//    public AuthenticationResponseDto handleInitialRequest(AuthenticationRequestDto registerDto){
//        validateClient(registerDto);
//        Client client = clientRepository
//                .findByFullPath(registerDto.baseUrl() + registerDto.endpoint())
//                .orElseGet(() -> {
//                    Client newClient = handleNewClient(registerDto);
//                    log.info("New client base URL: {}", newClient.getBaseUrl());
//
//                    return newClient;
//                });
//
//        Credential credential = credentialService.resolveCredential(client);
//        client.getCredentials().add(credential);
//        clientRepository.save(client);
//
//        log.info("New credential created for service: {}", client.getServiceName());
//
//        return new AuthenticationResponseDto(credential.getCredential());
//    }