package com.payme.authentication.service;

import com.payme.authentication.components.TokenServiceClient;
import com.payme.authentication.entity.Role;
import com.payme.authentication.entity.User;
import com.payme.authentication.exception.BadRequestException;
import com.payme.authentication.exception.DuplicateCredentialException;
import com.payme.authentication.exception.UserNotFoundException;
import com.payme.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * Creates and persists a new {@link User} with the provided credentials and roles.
     * <p>
     * This method encodes the user's password, initializes account status flags,
     * and checks for duplicate username or email entries before saving.
     * </p>
     *
     * @param username the desired username for the new user; must be unique
     * @param email the user's email address; must be unique
     * @param password the raw password to be encoded and stored securely
     * @param roles a set of roles to assign to the new user
     * @return the created {@link User} entity
     * @throws DuplicateCredentialException if the username or email already exists
     */
    @Transactional
    public User createNewUser(String username, String email, String password, Set<Role> roles){

        if(userRepository.existsByUsernameOrEmail(username, email)){
            throw new DuplicateCredentialException("Username or Email already registered. ");
        }

        return userRepository.save(
                User.builder()
                        .username(username)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .roles(roles)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(false)
                        .build()
        );
    }

    // map to dto and return a dto
    public User findById(UUID userId){
        log.info("User fetched with ID: {}", userId);
        return userRepository
                .findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with ID " + userId + " not found. "));
    }

}
