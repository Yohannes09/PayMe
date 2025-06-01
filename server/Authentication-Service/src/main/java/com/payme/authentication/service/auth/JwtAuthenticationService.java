package com.payme.authentication.service.auth;

import com.payme.authentication.component.TokenProvider;
import com.payme.authentication.constant.DefaultRoles;
import com.payme.authentication.dto.authentication.LoginRequest;
import com.payme.authentication.dto.authentication.RegistrationRequest;
import com.payme.authentication.entity.User;
import com.payme.authentication.entity.Role;
import com.payme.authentication.component.UserAccountManager;
import com.payme.authentication.component.RoleProvider;
import com.payme.authentication.dto.authentication.AuthenticationResponse;
import com.payme.internal.security.constant.TokenRecipient;
import com.payme.internal.security.model.UserTokenSubject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("jwtAuthenticationService")
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationService implements AuthenticationService {
    private final RoleProvider roleProvider;
    private final UserAccountManager userAccountManager;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public void register(RegistrationRequest registrationRequest) {
        userAccountManager.createNewUser(
                registrationRequest.username(),
                registrationRequest.email(),
                registrationRequest.password(),
                fetchDefaultRoles()
        );

        log.info("Successful registration: {}", registrationRequest.username());
    }


    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        User user = null;
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.usernameOrEmail(), loginRequest.password()
                )
        );

        if(authentication.getPrincipal() instanceof User u)
            user =  u;
        else
            throw new IllegalStateException("Error - Incompatible types\n -Expected: User\n -Returned: " + authentication.getPrincipal().getClass());

        log.info("Successful login: {}", user.getId());
        return generateAuthenticationResponse(user);
    }


    @Override
    public AuthenticationResponse refresh(UUID id){
        User user = userAccountManager.findById(id);
        validateUserAccount(user);

        log.info("Token refresh: {}", user.getId());
        return generateAuthenticationResponse(user);
    }


    @Override
    public void logout(String token){
        // No logic here yet.
    }


    private Set<Role> fetchDefaultRoles(){
        Set<Role> defaultRoles = new HashSet<>();
        Role userRole = roleProvider.findRole(DefaultRoles.USER.getRole());
        defaultRoles.add(userRole);

        return defaultRoles;
    }


    private AuthenticationResponse generateAuthenticationResponse(User user){
        UUID id = user.getId();
        Set<String> roles = user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());

        String accessToken = tokenProvider.generateAccessToken(
                new UserTokenSubject(id.toString(), roles), TokenRecipient.USER
        );

        String refreshToken = tokenProvider.generateRefreshToken(
                new UserTokenSubject(id.toString(), roles), TokenRecipient.USER
        );

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .usernameOrEmail(user.getUsername())
                .userId(id)
                .build();
    }


    private void validateUserAccount(User user) {
        if (!user.isAccountNonLocked()) {
            throw new LockedException("Account is locked");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("Account is disabled");
        }
        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("Account has expired");
        }
        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("Credentials have expired");
        }

    }


}

