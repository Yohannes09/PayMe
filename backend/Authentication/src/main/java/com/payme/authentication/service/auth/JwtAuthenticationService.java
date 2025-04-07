package com.payme.authentication.service.auth;

import com.payme.authentication.entity.SecurityUser;
import com.payme.authentication.entity.Role;
import com.payme.authentication.service.SecurityUserService;
import com.payme.authentication.service.RoleService;
import com.payme.authentication.service.token.TokenService;
import com.payme.authentication.constant.PaymeRoles;
import com.payme.authentication.dto.AuthenticationResponseDto;
import com.payme.authentication.dto.LoginDto;
import com.payme.authentication.dto.RegisterDto;
import com.payme.authentication.exception.DuplicateCredentialException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
@Qualifier("JwtAuthenticationService")
public class JwtAuthenticationService implements AuthenticationService {
    private final RoleService roleService;
    private final TokenService tokenService;
    private final SecurityUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public void register(@Valid RegisterDto registerDto) {
        if(userService.isUsernameOrEmailTaken(registerDto.username(), registerDto.email())) {
            throw new DuplicateCredentialException("Username or email already in use. ");
        }

        SecurityUser user = createNewUserWithRoles(registerDto);



        log.info("User registered successfully: {}", user.getUsername());
    }


    @Override
    public AuthenticationResponseDto login(@Valid LoginDto loginDto) {
        SecurityUser authenticatedUser = authenticate(loginDto);

        AuthenticationResponseDto response = tokenService.initializeUserSession(authenticatedUser);
        log.info("Successful login for ID: {}", authenticatedUser.getId());
        return response;
    }


    public void logout(String token){
        tokenService.deleteByToken(token);
    }


    private SecurityUser authenticate(LoginDto loginCredentials){
        String credential = Optional.ofNullable(loginCredentials.usernameOrEmail()).orElse("");
        log.info("Authenticating user : {}", credential);

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginCredentials.usernameOrEmail(),
                            loginCredentials.password()
                    )
            );

            return (SecurityUser) authentication.getPrincipal();
        }catch (BadCredentialsException e){
            throw new BadCredentialsException(credential + " provided bad credentials");
        }catch (DisabledException e){
            throw new DisabledException(credential + " has been disabled");
        }

    }

    private SecurityUser createNewUserWithRoles(RegisterDto registerDto){
        Set<Role> defaultRoles = new HashSet<>();
        Role userRole = roleService.findRole(PaymeRoles.USER);
        defaultRoles.add(userRole);

        SecurityUser securityUser = userService.createNewSecurityUser(
                registerDto.username(),
                registerDto.email(),
                passwordEncoder.encode(registerDto.password()),
                defaultRoles
        );

        return securityUser;
    }

}

