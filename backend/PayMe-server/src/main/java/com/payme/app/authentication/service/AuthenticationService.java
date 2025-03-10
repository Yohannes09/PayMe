package com.payme.app.authentication.service;

import com.payme.app.authentication.TokenRepository;
import com.payme.app.authentication.entity.SessionToken;
import com.payme.app.entity.User;
import com.payme.app.constants.PaymeRoles;
import com.payme.app.authentication.dto.AuthenticationResponseDto;
import com.payme.app.authentication.dto.LoginDto;
import com.payme.app.authentication.dto.RegisterDto;
import com.payme.app.exception.BadRequestException;
import com.payme.app.exception.DuplicateCredentialException;
import com.payme.app.exception.UserNotFoundException;
import com.payme.app.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

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


    public AuthenticationResponseDto register(RegisterDto registerDto) {
        try {
            if(userRepository.isCredentialTaken(registerDto.getUsername(), registerDto.getEmail())) {
                throw new DuplicateCredentialException("Username or email already in use. ");
            }

            User generatedUserFromDto = generateUserFromDto(registerDto);
            User savedUser = userRepository.save(generatedUserFromDto);
            log.info("User registered successfully: {}", savedUser.getUserId());

            String jwtToken = jwtService.generateToken(savedUser);
            createUserSession(savedUser, jwtToken);
            return generateAuthenticationResponse(savedUser, jwtToken);
        } catch (DuplicateCredentialException e) {
            throw new RuntimeException(e);
        }
    }


    public AuthenticationResponseDto login2(LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        User user = fetchUser(loginDto.getUsernameOrEmail());
        String newToken = jwtService.generateToken(user);
        createUserSession(user, newToken);

        return generateAuthenticationResponse(user, newToken);
    }

//    public AuthenticationResponseDto login(LoginDto loginDto) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginDto.getUsernameOrEmail(),
//                        loginDto.getPassword()
//                )
//        );
//
//        User user = fetchUser(loginDto.getUsernameOrEmail());
//
//        String jwtToken = tokenRepository
//                .findByUserId(user.getUserId())
//                .filter(token -> !jwtService.isTokenExpired(token.getToken()))
//                .map(SessionToken::getToken)
//                .orElseGet(()-> {
//                            String newJwt = jwtService.generateToken(user);
//                            createUserSession(user, newJwt);
//                            return newJwt;
//                });
//
//        return generateAuthenticationResponse(user, jwtToken);
//    }


    private void createUserSession(User user, String token){
        log.info("Generating token for user: {}", user.getUserId());
        Date creationTime = jwtService.extractClaim(token, Claims::getIssuedAt);
        Date expirationTime = jwtService.extractClaim(token, Claims::getExpiration);

        SessionToken sessionToken = SessionToken.builder()
                .user(user)
                .token(token)
                .createdAt(creationTime)
                .expiresAt(expirationTime)
                .build();

        tokenRepository.save(sessionToken);
    }


    public void logout(String token){
        tokenRepository.deleteByToken(token);
    }


    private AuthenticationResponseDto generateAuthenticationResponse(User user, String jwtToken){
        return AuthenticationResponseDto.builder()
                .userId(user.getUserId())
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }


    private User generateUserFromDto(RegisterDto registerDto){
         return User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .accounts(new ArrayList<>())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(Set.of(PaymeRoles.USER))
                .isActive(true)
                .build();
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

