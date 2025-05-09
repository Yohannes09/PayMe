package com.payme.authentication.service;

import com.payme.authentication.components.TokenServiceClient;
import com.payme.authentication.entity.Role;
import com.payme.authentication.entity.User;
import com.payme.authentication.exception.BadRequestException;
import com.payme.authentication.exception.UserNotFoundException;
import com.payme.authentication.repository.UserRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final RestClient restClient;
    private final TokenServiceClient tokenServiceClient;
    private final String userServiceUrl;
    private final String userEndpoint;

    public UserService(
            UserRepository userRepository,
            RestClient restClient,
            TokenServiceClient tokenServiceClient
    ) {
        this.userRepository = userRepository;
        this.restClient = restClient;
        this.tokenServiceClient = tokenServiceClient;
        this.userServiceUrl = tokenServiceClient.getBaseUrl();
        this.userEndpoint = tokenServiceClient.getUserEndpoint();
    }

    @Transactional
    public User createNewUser(
            String username,
            String email,
            String password,
            Set<Role> roles
    ) {
        User user = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .roles(roles)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(false)
                .build();

        User savedUser = userRepository.save(user);
        newUserPostRequest(savedUser.getId());
        return savedUser;
    }

    // map to dto and return a dto
    public User findById(UUID userId){
        log.info("User fetched with ID: {}", userId);
        return userRepository
                .findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with ID " + userId + " not found. "));
    }

    public boolean isUsernameOrEmailTaken(String username, String email) {
        boolean isUsernamePresent = Optional.ofNullable(username).isPresent();
        boolean isEmailPresent = Optional.ofNullable(email).isPresent();

        if(!isUsernamePresent && !isEmailPresent){
            throw new BadRequestException("");
        }
        if(isUsernamePresent && isEmailPresent) {
            return userRepository.existsByUsernameOrEmail(username, email);
        }
        if(isUsernamePresent) {
            return userRepository.existsByUsernameIgnoreCase(username);
        }
        if(isEmailPresent) {
            return userRepository.existsByEmailIgnoreCase(email);
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
