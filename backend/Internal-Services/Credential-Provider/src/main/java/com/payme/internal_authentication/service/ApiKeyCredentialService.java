package com.payme.internal_authentication.service;

import com.payme.internal_authentication.constant.CredentialStatus;
import com.payme.internal_authentication.entity.Client;
import com.payme.internal_authentication.entity.Credential;
import com.payme.internal_authentication.util.CredentialGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

//@Slf4j
//@Service
//public class ApiKeyCredentialService implements CredentialService{
//    private static final int CREDENTIAL_LENGTH = 32;
//    private static final int CREDENTIAL_VALIDITY_HOURS = 2;
//
//    @Override
//    public Credential resolveCredential(Client client){
//        return resolveClientCredential(client);
//    }
//
//
//    private Credential resolveClientCredential(Client client){
//        invalidateExpiredCredentials(client.getCredentials());
//
//        List<Credential> validClientCredentials = client.getCredentials()
//                .stream()
//                .filter(this::isCredentialValid)
//                .toList();
//
//        return validClientCredentials
//                .stream()
//                .min(Comparator.comparing(Credential::getExpiresAt))
//                .orElse(generateNewCredential(client, CredentialStatus.VALID));
//    }
//
//    private void invalidateExpiredCredentials(List<Credential> credentials){
//        List<Credential> expiredCredentials = credentials.stream()
//                .filter(this::isCredentialExpired)
//                .toList();
//
//        expiredCredentials.forEach(credential -> credential.setCredentialStatus(CredentialStatus.EXPIRED));
//    }
//
//    private boolean isCredentialValid(Credential credential){
//        return !isCredentialExpired(credential) && isCredentialStatusValid(credential);
//    }
//
//    private Credential generateNewCredential(
//            Client client,
//            CredentialStatus credentialStatus
//    ){
//        String credential = CredentialGenerator.generateCredential(CREDENTIAL_LENGTH);
//
//        return Credential.builder()
//                .credential(credential)
//                .client(client)
//                .credentialStatus(credentialStatus)
//                .expiresAt(LocalDateTime.now().plusHours(CREDENTIAL_VALIDITY_HOURS))
//                .build();
//    }
//
//    private boolean isCredentialExpired(Credential credential){
//        return credential.getExpiresAt().isBefore(LocalDateTime.now());
//    }
//
//    private boolean isCredentialStatusValid(Credential credential){
//        return credential.getCredentialStatus().equals(CredentialStatus.VALID);
//    }
//
//}
