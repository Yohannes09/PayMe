package com.payme.authentication.service;

import com.payme.authentication.repository.TokenRepository;
import com.payme.authentication.entity.Token;
import com.payme.authentication.entity.Role;
import com.payme.authentication.entity.User;
import com.payme.authentication.constant.PaymeRoles;
import com.payme.authentication.dto.AuthenticationResponseDto;
import com.payme.authentication.dto.LoginDto;
import com.payme.authentication.dto.RegisterDto;
import com.payme.authentication.exception.DuplicateCredentialException;
import com.payme.authentication.repository.UserRepository;
import io.jsonwebtoken.Claims;
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
public class JwtAuthenticationService implements AuthenticationService{
    private static final int INITIAL_ACCOUNT_SIZE = 3;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final RoleService roleService;


    public JwtAuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            TokenRepository tokenRepository,
            RoleService roleService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.roleService = roleService;
    }


    @Override
    @Transactional
    public void register(@Valid RegisterDto registerDto) {
        if(userRepository.isCredentialTaken(registerDto.username(), registerDto.email())) {
            throw new DuplicateCredentialException("Username or email already in use. ");
        }

        User generatedUserFromDto = createNewUser(registerDto);
        User savedUser = userRepository.save(generatedUserFromDto);

        log.info("User registered successfully: {}", savedUser.getUsername());
    }


    @Override
    public AuthenticationResponseDto login(@Valid LoginDto loginDto) {
        User authenticatedUser = authenticateUser(loginDto);

        manageUserSessions(authenticatedUser);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        createUserSession(authenticatedUser, jwtToken);

        return generateAuthenticationResponse(authenticatedUser, jwtToken);
    }


    public void logout(String token){
        tokenRepository.deleteByToken(token);
    }


    private User authenticateUser(LoginDto loginCredentials){
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
            return (User) authentication.getPrincipal();

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

    private void manageUserSessions(User user){
        log.info("Managing user sessions. ");
        List<Token> userActiveSessions = tokenRepository
                .findAllByUserId(user.getUserId())
                .stream()
                .filter(token -> {
                    return jwtService.isTokenValid(token.getToken(), user.getUsername());
                })
                .toList();

        if(userActiveSessions.size() >= 5){
            userActiveSessions.stream()
                    .min(Comparator.comparing(Token::getCreatedAt))
                    .map(Token::getToken)
                    .ifPresent(tokenRepository::deleteByToken);
        }
    }

    private void createUserSession(User user, String token){
        Date creationTime = jwtService.extractClaim(token, Claims::getIssuedAt);
        Date expirationTime = jwtService.extractClaim(token, Claims::getExpiration);

        if (user.isEnabled()) {
            log.info("Creating session for user: {}", user.getUsername());
            Token sessionToken = Token.builder()
                    .user(user)
                    .token(token)
                    .isValid(true)
                    .createdAt(creationTime)
                    .expiresAt(expirationTime)
                    .build();

            tokenRepository.save(sessionToken);
        }
    }

    private AuthenticationResponseDto generateAuthenticationResponse(User user, String jwtToken){
        return AuthenticationResponseDto.builder()
                .userId(user.getUserId())
                .token(jwtToken)
                .build();
    }

    private User createNewUser(RegisterDto registerDto){
        Set<Role> defaultRoles = new HashSet<>();
        Role defaultRole = roleService.fetchRole(PaymeRoles.USER);
        defaultRoles.add(defaultRole);

        return User.builder()
                .firstName(registerDto.firstName())
                .lastName(registerDto.lastName())
                .username(registerDto.username())
                .email(registerDto.email())
                .password(passwordEncoder.encode(registerDto.password()))
                .roles(defaultRoles)
                .active(false)
                .build();

    }


}

