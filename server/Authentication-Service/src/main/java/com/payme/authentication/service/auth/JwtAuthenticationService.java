package com.payme.authentication.service.auth;

import com.payme.authentication.components.TokenProvider;
import com.payme.authentication.constant.DefaultRoles;
import com.payme.authentication.dto.authentication.LoginRequest;
import com.payme.authentication.dto.authentication.RegististrationRequest;
import com.payme.authentication.entity.User;
import com.payme.authentication.entity.Role;
import com.payme.authentication.exception.DuplicateCredentialException;
import com.payme.authentication.service.UserService;
import com.payme.authentication.components.RoleProvider;
import com.payme.authentication.dto.authentication.AuthenticationResponse;
import com.payme.internal.security.constant.TokenRecipient;
import com.payme.internal.security.model.UserTokenSubject;
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
    /*
    * TO DO:
    *   POLISH ERROR HANDLING
    *   */
    private final RoleProvider roleProvider;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public void register(RegististrationRequest regististrationRequest) {
        try {
            userService.createNewUser(
                    regististrationRequest.username(),
                    regististrationRequest.email(),
                    regististrationRequest.password(),
                    fetchDefaultRoles()
            );

            log.info("User registered successfully: {}", regististrationRequest.username());

        } catch (DuplicateCredentialException e) {
            log.warn("Registration failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }


    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        User user = authenticate(loginRequest.usernameOrEmail(), loginRequest.password());

        UUID userId = user.getId();
        Set<String> userRoles = user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());

        log.info("Successful login for ID: {}", user.getId());
        return generateAuthenticationResponse(userId, userRoles);
    }

    public AuthenticationResponse refresh(){
        return null;
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

            throw new IllegalStateException("Error - Incompatible types\n -Expected: User" + "\n -Returned: " + principal.getClass());

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

    private AuthenticationResponse generateAuthenticationResponse(UUID id, Set<String> roles){
        String accessToken = tokenProvider.generateAccessToken(
                new UserTokenSubject(id.toString(), roles), TokenRecipient.USER
        );

        String refreshToken = tokenProvider.generateRefreshToken(
                new UserTokenSubject(id.toString(), roles), TokenRecipient.USER
        );

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(id)
                .build();
    }

}

