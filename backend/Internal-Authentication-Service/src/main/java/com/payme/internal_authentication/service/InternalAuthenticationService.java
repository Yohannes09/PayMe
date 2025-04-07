package com.payme.internal_authentication.service;

import com.payme.internal_authentication.entity.ServiceClient;
import com.payme.internal_authentication.util.CredentialGenerator;
import org.springframework.stereotype.Service;
import com.payme.internal_authentication.constant.CredentialStatus;
import com.payme.internal_authentication.constant.ServiceType;
import com.payme.internal_authentication.dto.AuthenticationRequestDto;
import com.payme.internal_authentication.dto.AuthenticationResponseDto;
import com.payme.internal_authentication.entity.ServiceCredential;
import com.payme.internal_authentication.repository.ServiceCredentialRepository;
import com.payme.internal_authentication.repository.ServiceClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class InternalAuthenticationService {
    private static final int MIN_CREDENTIAL_LENGTH = 32;

    private final ServiceClientRepository serviceClientRepository;
    private final ServiceCredentialRepository serviceCredentialRepository;

    // Each time client starts, it sends its information.
    public AuthenticationResponseDto handleInitialRequest(AuthenticationRequestDto requestDto){
        Optional<ServiceClient> service = serviceClientRepository.findByServiceName(requestDto.serviceName());

        if(service.isPresent()){

        }
        return null;
    }

    private LocalDateTime getCredentialValidity(){
        return LocalDateTime.now().plusHours(2);
    }

    private String generateKey(){
        return "";
    }

    private boolean isExpired(LocalDateTime expirationTime){
        return expirationTime.isBefore(LocalDateTime.now());
    }

    private String handleExistingServiceClient(ServiceClient serviceClient){
        List<ServiceCredential> clientCredentials = serviceClient.getServiceCredential()
                .stream()
                .peek(credential -> {
                    if(isExpired(credential.getExpiresAt()) && !credential.getCredentialStatus().equals(CredentialStatus.EXPIRED)){
                        credential.setCredentialStatus(CredentialStatus.EXPIRED);
                    }
                })
                .filter(credential -> !isExpired(credential.getExpiresAt()))
                .toList();

        String activeCredential = clientCredentials
                .stream()
                .min(Comparator.comparing(ServiceCredential::getExpiresAt))
                .map(credential ->  credential.getCredential())
                .orElse(CredentialGenerator.generateCredential(MIN_CREDENTIAL_LENGTH));

        return activeCredential;
    }

    private ServiceClient handleNewServiceClient(AuthenticationRequestDto requestDto){
        ServiceClient newServiceClient = generateServiceClient(
                requestDto.serviceName(),
                requestDto.baseUrl(),
                requestDto.endpoint(),
                requestDto.description(),
                ServiceType.INTERNAL
        );
        return serviceClientRepository.save(newServiceClient);
    }

    private ServiceClient generateServiceClient(
            String serviceName,
            String baseUrl,
            String endpoint,
            String description,
            ServiceType serviceType
    ){
        String desc = Optional.ofNullable(description).orElse("");

        return ServiceClient.builder()
                .serviceName(serviceName)
                .baseUrl(baseUrl)
                .endpoint(endpoint)
                .description(desc)
                .serviceType(serviceType)
                .build();
    }

    private ServiceCredential generateCredential(
            String credential,
            ServiceClient service,
            CredentialStatus credentialStatus,
            LocalDateTime expiresAt

    ){
        return ServiceCredential.builder()
                .credential(credential)
                .serviceClient(service)
                .credentialStatus(credentialStatus)
                .expiresAt(expiresAt)
                .build();
    }
}
