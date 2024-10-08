package com.tenmo.authentication;

import com.tenmo.authentication.dto.AuthenticationResponseDto;
import com.tenmo.authentication.dto.LoginDto;
import com.tenmo.authentication.dto.RegisterDto;
import com.tenmo.entity.User;
import com.tenmo.exception.DuplicateCredentialException;
import com.tenmo.exception.NotFoundException;
import com.tenmo.repository.UserRepository;
import com.tenmo.util.TenmoRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponseDto register(RegisterDto registerDto) {

        try {
            if(userExists(registerDto.getEmail()))
                throw new DuplicateCredentialException("Username already taken. ");

            if(userExists(registerDto.getUsername()))
                throw new DuplicateCredentialException("Email already registered. ");

            var user = User.builder()
                    .firstName(registerDto.getFirstName())
                    .lastName(registerDto.getLastName())
                    .email(registerDto.getEmail())
                    .password(passwordEncoder.encode(registerDto.getPassword()))
                    .role(TenmoRoles.USER)
                    .build();

            var updatedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponseDto.builder()
                    .userId(updatedUser.getUserId())
                    .token(jwtToken)
                    .firstName(registerDto.getFirstName())
                    .lastName(registerDto.getLastName())
                    .email(registerDto.getEmail())
                    .build();

        } catch (DuplicateCredentialException e) {
            throw new RuntimeException(e);
        }
    }


    public AuthenticationResponseDto login(LoginDto loginDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(),
                        loginDto.getPassword())
        );

        var user = userRepository
                .findByUsernameOrEmail(loginDto.getUsernameOrEmail())
                .orElseThrow(()-> new NotFoundException("Could not retrieve account. "));

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponseDto.builder()
                .userId(user.getUserId())
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    private boolean userExists(String usernameOrEmail){
        return userRepository.findByUsernameOrEmail(usernameOrEmail).orElse(null) != null;
    }

}

