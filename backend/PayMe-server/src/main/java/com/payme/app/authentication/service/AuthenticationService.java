package com.payme.app.authentication.service;

import com.payme.app.authentication.TokenRepository;
import com.payme.app.authentication.entity.SessionToken;
import com.payme.app.authentication.util.UserPrincipal;
import com.payme.app.entity.User;
import com.payme.app.constants.PaymeRoles;
import com.payme.app.authentication.dto.AuthenticationResponseDto;
import com.payme.app.authentication.dto.LoginDto;
import com.payme.app.authentication.dto.RegisterDto;
import com.payme.app.exception.DuplicateCredentialException;
import com.payme.app.exception.UserNotFoundException;
import com.payme.app.mapper.UserMapper;
import com.payme.app.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }


    public AuthenticationResponseDto register(@Valid RegisterDto registerDto) {
        if(userRepository.isCredentialTaken(registerDto.getUsername(), registerDto.getEmail())) {
            throw new DuplicateCredentialException("Username or email already in use. ");
        }

        User generatedUserFromDto = generateUserFromDto(registerDto);
        User savedUser = userRepository.save(generatedUserFromDto);

        log.info("User registered successfully: {}", savedUser.getUsername());

        UserPrincipal userPrincipal = UserMapper.mapUserToPrincipal(savedUser);
        String jwtToken = jwtService.generateToken(userPrincipal);

        createUserSession(savedUser, jwtToken);

        return generateAuthenticationResponse(userPrincipal, jwtToken);
    }

    public AuthenticationResponseDto login(LoginDto loginDto) {
        Authentication authenticatedUser = authenticateUser(loginDto);
        log.info(authenticatedUser.getName());
        User fetchedUser = fetchUser(authenticatedUser.getName());
        manageUserSessions(fetchedUser.getUserId());

        UserPrincipal userPrincipal = UserMapper.mapUserToPrincipal(fetchedUser);
        String jwtToken = jwtService.generateToken(userPrincipal);

        createUserSession(fetchedUser, jwtToken);
        return generateAuthenticationResponse(userPrincipal, jwtToken);
    }

    public void logout(String token){
        tokenRepository.deleteByToken(token);
    }


    private Authentication authenticateUser(LoginDto loginCredentials){
        String credential = Optional.ofNullable(loginCredentials.getUsernameOrEmail()).orElse("");
        log.info("Authenticating user : {}", credential);

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginCredentials.getUsernameOrEmail(),
                            loginCredentials.getPassword()
                    )
            );

            log.info("Authentication successful for user: {}", credential);
            return authentication;
        }catch (AuthenticationException e){
            log.warn("Authentication failed for user: {}", credential);
            log.error("Error during authentication: ", e);
            throw new BadCredentialsException("Could not find user: " + credential);
        }
    }

    private void manageUserSessions(UUID userId){
        log.info("Managing user sessions. ");
        List<SessionToken> userActiveSessions = tokenRepository.findAllByUserId(userId);

        if(userActiveSessions.size() >= 5){
            userActiveSessions.stream()
                    .min(Comparator.comparing(SessionToken::getCreatedAt))
                    .map(SessionToken::getToken)
                    .ifPresent(tokenRepository::deleteByToken);
        }
    }

    private void createUserSession(User user, String token){
        log.info("Generating token for user: {}", user.getUserId());
        Date creationTime = jwtService.extractClaim(token, Claims::getIssuedAt);
        Date expirationTime = jwtService.extractClaim(token, Claims::getExpiration);

        SessionToken sessionToken = SessionToken.builder()
                .user(user)
                .token(token)
                .isValid(true)
                .createdAt(creationTime)
                .expiresAt(expirationTime)
                .build();
        tokenRepository.save(sessionToken);
    }

    private AuthenticationResponseDto generateAuthenticationResponse(UserPrincipal user, String jwtToken){
        var response = AuthenticationResponseDto.builder()
                .userId(user.getUserId())
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
        return response;
    }

    private User generateUserFromDto(RegisterDto registerDto){
        Set<PaymeRoles> defaultRoles = new HashSet<>();
        defaultRoles.add(PaymeRoles.USER);

        User user = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .accounts(new ArrayList<>())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(defaultRoles)
                .active(false)
                .build();

        log.info("Generated user: {}", user);
         return user;
    }

    private User fetchUser(UUID userId){
        log.info("User fetched with ID: {}", userId);
        return userRepository
                .findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with ID " + userId + " not found. "));
    }

    private User fetchUser(String usernameOrEmail){
        log.info("Retrieving user with credential: {}", usernameOrEmail);
        return userRepository
                .findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(()-> new UserNotFoundException("User with credential " + usernameOrEmail + " not found. "));
    }
}

