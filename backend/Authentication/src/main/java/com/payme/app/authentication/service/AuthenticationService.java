package com.payme.app.authentication.service;

import com.payme.app.authentication.TokenRepository;
import com.payme.app.authentication.entity.SessionToken;
import com.payme.app.entity.Role;
import com.payme.app.entity.User;
import com.payme.app.constants.PaymeRoles;
import com.payme.app.authentication.dto.AuthenticationResponseDto;
import com.payme.app.authentication.dto.LoginDto;
import com.payme.app.authentication.dto.RegisterDto;
import com.payme.app.exception.DuplicateCredentialException;
import com.payme.app.exception.RoleNotFoundException;
import com.payme.app.repository.RoleRepository;
import com.payme.app.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;


    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            TokenRepository tokenRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
    }


    @Transactional
    public void register(@Valid RegisterDto registerDto) {
        if(userRepository.isCredentialTaken(registerDto.username(), registerDto.email())) {
            throw new DuplicateCredentialException("Username or email already in use. ");
        }

        User generatedUserFromDto = generateUserFromDto(registerDto);
        User savedUser = userRepository.save(generatedUserFromDto);

        log.info("User registered successfully: {}", savedUser.getUsername());
    }


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
        List<SessionToken> userActiveSessions = tokenRepository
                .findAllByUserId(user.getUserId())
                .stream()
                .filter(token -> {
                    return jwtService.isTokenValid(token.getToken(), user.getUsername());
                })
                .collect(Collectors.toList());

        if(userActiveSessions.size() >= 5){
            userActiveSessions.stream()
                    .min(Comparator.comparing(SessionToken::getCreatedAt))
                    .map(SessionToken::getToken)
                    .ifPresent(tokenRepository::deleteByToken);
        }
    }

    private void createUserSession(User user, String token){
        Date creationTime = jwtService.extractClaim(token, Claims::getIssuedAt);
        Date expirationTime = jwtService.extractClaim(token, Claims::getExpiration);

        if (user.isEnabled()) {
            log.info("Creating session for user: {}", user.getUsername());
            SessionToken sessionToken = SessionToken.builder()
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

    private User generateUserFromDto(RegisterDto registerDto){
        Set<Role> defaultRoles = new HashSet<>();
        Role defaultRole = roleRepository.findByRole(PaymeRoles.USER)
                .orElseThrow(()-> new RoleNotFoundException("Could not fetch default role for user. "));
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

    @PostConstruct
    private void addAllroles(){
        List<Role> roles = List.of(
                new Role(PaymeRoles.USER),
                new Role(PaymeRoles.ADMIN),
                new Role(PaymeRoles.SUPER_ADMIN)
        );

        roles.forEach(role -> {
                try {
                    if (!roleRepository.existsByRole(role.getRole())){
                        roleRepository.save(role);
                        log.info("Saved new role: {}", role.getRole());
                    }
                } catch (Exception e) {
                    log.error("Error encountered while saving role. ");
                }
            }
        );

    }


}

