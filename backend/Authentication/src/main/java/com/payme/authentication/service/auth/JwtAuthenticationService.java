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

@Service
@Qualifier("JwtAuthenticationService")
@Slf4j
public class JwtAuthenticationService implements AuthenticationService {
    private final SecurityUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;

    public JwtAuthenticationService(
            SecurityUserService userService,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            AuthenticationManager authenticationManager,
            RoleService roleService
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
    }


    @Override
    @Transactional
    public void register(@Valid RegisterDto registerDto) {
        if(userService.isCredentialTaken(registerDto.username(), registerDto.email())) {
            throw new DuplicateCredentialException("Username or email already in use. ");
        }

        SecurityUser generatedUser = createNewUserWithRoles(registerDto);
        SecurityUser savedUser = userService.save(generatedUser);

        log.info("User registered successfully: {}", savedUser.getUsername());
    }


    @Override
    public AuthenticationResponseDto login(@Valid LoginDto loginDto) {
        SecurityUser authenticatedUser = authenticateUser(loginDto);

        return tokenService.initializeUserSession(authenticatedUser);
    }


    public void logout(String token){
        tokenService.deleteByToken(token);
    }


    private SecurityUser authenticateUser(LoginDto loginCredentials){
        String credential = Optional.ofNullable(loginCredentials.usernameOrEmail()).orElse("");
        log.info("Authenticating user : {}", credential);

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginCredentials.usernameOrEmail(),
                            loginCredentials.password()
                    )
            );

            log.info("Authentication successful for user: {}", credential);
            return (SecurityUser) authentication.getPrincipal();

        }catch (BadCredentialsException e){
            log.warn("Authentication failed for user: {}", credential);
            log.error("Error during authentication: ", e);
            throw new BadCredentialsException("Could not find user: " + credential);
        }catch (DisabledException e){
            log.warn("Authentication failed for user: {}", credential);
            log.error("Error during authentication: ", e);
            throw new DisabledException("Authentication failed for disabled user: " + credential);
        }

    }


    private SecurityUser createNewUserWithRoles(RegisterDto registerDto){
        Set<Role> defaultRoles = new HashSet<>();
        Role defaultRole = roleService.fetchRole(PaymeRoles.USER);
        defaultRoles.add(defaultRole);

        SecurityUser user = userService.createUser(registerDto);

        return user.builder()
                .password(passwordEncoder.encode(registerDto.password()))
                .roles(defaultRoles)
                .build();
    }


}

