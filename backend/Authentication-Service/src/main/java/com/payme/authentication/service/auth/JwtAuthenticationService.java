package com.payme.authentication.service.auth;

import com.payme.authentication.entity.User;
import com.payme.authentication.entity.Role;
import com.payme.authentication.service.UserService;
import com.payme.authentication.components.RoleProvider;
import com.payme.authentication.dto.AuthenticationResponseDto;
import com.payme.authentication.dto.LoginDto;
import com.payme.authentication.dto.RegisterDto;
import com.payme.authentication.exception.DuplicateCredentialException;
import com.payme.internal.security.constant.PaymeRoles;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("JwtAuthenticationService")
public class JwtAuthenticationService implements AuthenticationService {
    private final RoleProvider roleProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public void register(RegisterDto registerDto) {
        if(userService.isUsernameOrEmailTaken(registerDto.username(), registerDto.email())) {
            throw new DuplicateCredentialException("Username or email already in use. ");
        }

        User user = createNewUserWithRoles(registerDto);
        log.info("User registered successfully: {}", user.getUsername());
    }


    @Override
    public AuthenticationResponseDto login(@Valid LoginDto loginDto) {
        User user = authenticate(loginDto);


        log.info("Successful login for ID: {}", user.getId());
        return null;
    }


    public void logout(String token){

    }


    private User authenticate(LoginDto loginCredentials){
        String credential = Optional.ofNullable(loginCredentials.usernameOrEmail()).orElse("");
        log.info("Authenticating user : {}", credential);

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginCredentials.usernameOrEmail(),
                            loginCredentials.password()
                    )
            );

            return (User) authentication.getPrincipal();
        }catch (BadCredentialsException e){
            throw new BadCredentialsException(credential + " provided bad credentials");
        }catch (DisabledException e){
            throw new DisabledException(credential + " has been disabled");
        }

    }

    private User createNewUserWithRoles(RegisterDto registerDto){
        Set<Role> defaultRoles = new HashSet<>();
        Role userRole = roleProvider.findRole(PaymeRoles.USER);
        defaultRoles.add(userRole);

        return userService.createNewUser(
                registerDto.username(),
                registerDto.email(),
                passwordEncoder.encode(registerDto.password()),
                defaultRoles
        );

    }

}

