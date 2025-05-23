package com.payme.authentication.service.auth;

import com.payme.authentication.components.TokenServiceClient;
import com.payme.authentication.constant.DefaultRoles;
import com.payme.authentication.dto.authentication.LoginRequest;
import com.payme.authentication.dto.authentication.RegisterRequest;
import com.payme.authentication.entity.User;
import com.payme.authentication.entity.Role;
import com.payme.authentication.exception.DuplicateCredentialException;
import com.payme.authentication.service.UserService;
import com.payme.authentication.components.RoleProvider;
import com.payme.authentication.dto.authentication.AuthenticationResponse;
import com.payme.internal.security.dto.TokenPairResponseDto;
import com.payme.internal.security.dto.UserTokenRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Qualifier("JwtAuthenticationService")
public class JwtAuthenticationService implements AuthenticationService {
    private final RoleProvider roleProvider;
    private final UserService userService;
    private final TokenServiceClient tokenServiceClient;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) {
        try {
            userService.createNewUser(
                    registerRequest.username(),
                    registerRequest.email(),
                    registerRequest.password(),
                    fetchDefaultRoles()
            );

            log.info("User registered successfully: {}", registerRequest.username());

        } catch (DuplicateCredentialException e) {
            log.warn("Registration failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }


    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        User user = authenticate(loginRequest.usernameOrEmail(), loginRequest.password());

        String username = user.getUsername();
        Set<String> roles = user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());

        TokenPairResponseDto response = tokenServiceClient.fetchAccessAndRefreshTokens(
                new UserTokenRequestDto(username, roles)
        );

        log.info("Successful login for ID: {}", user.getId());
        return AuthenticationResponse.builder()
                .accessToken(response.accessToken())
                .refreshToken(response.refreshToken())
                .userId(user.getId())
                .build();

    }

    @Override
    public void logout(String token){
        // No logic here yet.
    }


    private User authenticate(String usernameOrEmail, String password){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
            );

            Object principal = authentication.getPrincipal();

            if(principal instanceof User user){
                log.info("Login successful for ");
                return user;
            }
            log.error("Unexpected principle type: {}", principal.getClass());
            throw new IllegalStateException("Incompatible types");

        }catch (BadCredentialsException e){
            throw new BadCredentialsException(usernameOrEmail + " provided bad credentials");
        }catch (DisabledException e){
            throw new DisabledException(usernameOrEmail + " has been disabled");
        }

    }

    private Set<Role> fetchDefaultRoles(){
        Set<Role> defaultRoles = new HashSet<>();
        Role userRole = roleProvider.findRole(DefaultRoles.USER.getRole());
        defaultRoles.add(userRole);

        return defaultRoles;
    }

}

