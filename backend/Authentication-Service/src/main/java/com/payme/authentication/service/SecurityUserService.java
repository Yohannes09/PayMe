package com.payme.authentication.service;

import com.payme.authentication.configuration.RestClientConfig;
import com.payme.authentication.entity.Role;
import com.payme.authentication.entity.SecurityUser;
import com.payme.authentication.exception.BadRequestException;
import com.payme.authentication.exception.SecurityUserNotFoundException;
import com.payme.authentication.repository.SecurityUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Transactional
@Validated
@Service
public class SecurityUserService {
    private final SecurityUserRepository securityUserRepository;
    private final RestClient restClient;
    private final RestClientConfig restClientConfig;
    private final String userServiceUrl;
    private final String userEndpoint;

    public SecurityUserService(
            SecurityUserRepository securityUserRepository,
            RestClient restClient,
            RestClientConfig restClientConfig
    ) {
        this.securityUserRepository = securityUserRepository;
        this.restClient = restClient;
        this.restClientConfig = restClientConfig;
        this.userServiceUrl = restClientConfig.getBaseUrl();
        this.userEndpoint = restClientConfig.getUserEndpoint();
    }

    @Transactional
    public SecurityUser createNewSecurityUser(
            String username,
            String email,
            String password,
            Set<Role> roles
    ) {
        SecurityUser securityUser = SecurityUser.builder()
                .username(username)
                .email(email)
                .password(password)
                .roles(roles)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(false)
                .build();

        SecurityUser savedSecurityUser = securityUserRepository.save(securityUser);
        newUserPostRequest(savedSecurityUser.getId());
        return savedSecurityUser;
    }

    // map to dto and return a dto
    public SecurityUser findById(UUID userId){
        log.info("User fetched with ID: {}", userId);
        return securityUserRepository
                .findById(userId)
                .orElseThrow(()-> new SecurityUserNotFoundException("User with ID " + userId + " not found. "));
    }

    public boolean isUsernameOrEmailTaken(String username, String email) {
        boolean isUsernamePresent = Optional.ofNullable(username).isPresent();
        boolean isEmailPresent = Optional.ofNullable(email).isPresent();

        if(!isUsernamePresent && !isEmailPresent){
            throw new BadRequestException("");
        }
        if(isUsernamePresent && isEmailPresent) {
            return securityUserRepository.existsByUsernameOrEmail(username, email);
        }
        if(isUsernamePresent) {
            return securityUserRepository.existsByUsernameIgnoreCase(username);
        }
        if(isEmailPresent) {
            return securityUserRepository.existsByEmailIgnoreCase(email);
        }

        return true;
    }

    private void newUserPostRequest(UUID id){
        HttpStatusCode response = restClient.post()
                .uri(userServiceUrl+userEndpoint)
                .body(id)
                .retrieve()
                .toEntity(Void.class)
                .getStatusCode();

        if(response == null || !response.is2xxSuccessful()){
            throw new IllegalStateException("User-service failed to create user. ");
        }

    }

}
