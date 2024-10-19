package com.payme.app.authentication.service;

import com.payme.app.entity.User;
import com.payme.app.constants.TenmoRoles;
import com.payme.app.authentication.dto.AuthenticationResponseDto;
import com.payme.app.authentication.dto.LoginDto;
import com.payme.app.authentication.dto.RegisterDto;
import com.payme.app.exception.BadRequestException;
import com.payme.app.exception.DuplicateCredentialException;
import com.payme.app.exception.NotFoundException;
import com.payme.app.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Transactional
@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{5,15}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[0-9!@#$%^&*]).{6,}$";
    private static final String USERNAME_VALIDATION_MESSAGE = "Username should be 5-15 characters long with no special symbols.";
    private static final String PASSWORD_VALIDATION_MESSAGE = "Password must be 6+ characters long, have at least 1 capital letter, and a number or special character.";
    private static final String EMAIL_VALIDATION_MESSAGE = "Entered invalid email";

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
            if(isCredentialTaken(registerDto.getEmail()))
                throw new DuplicateCredentialException("Username already taken. ");

            if(isCredentialTaken(registerDto.getUsername()))
                throw new DuplicateCredentialException("Email already registered. ");

            var user = User.builder()
                    .firstName(registerDto.getFirstName())
                    .lastName(registerDto.getLastName())
                    .username(registerDto.getUsername())
                    .email(registerDto.getEmail())
                    .password(passwordEncoder.encode(registerDto.getPassword()))
                    .roles(Set.of(TenmoRoles.USER))
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


    public void updateUsername(UUID userId,
       @NonNull @Pattern(regexp = USERNAME_PATTERN
            ,message = USERNAME_VALIDATION_MESSAGE) String newUsername)
    throws BadRequestException {

        if(isCredentialTaken(newUsername))
            throw new BadRequestException("Username: " + newUsername + " taken. ");

        var user = fetchUser(userId);

        updateCredential(
                user,
                newUsername,
                user::setUsername,
                User::getUsername
        );
    }


    public void updatePassword(UUID userId,
       @NonNull @Pattern(regexp = PASSWORD_PATTERN,
               message = PASSWORD_VALIDATION_MESSAGE) String newPassword)
    throws BadRequestException{

        var user = fetchUser(userId);

        updateCredential(
                user,
                newPassword,
                user::setPassword,
                User::getPassword
        );
    }


    public void updateEmail(UUID userId,
        @NonNull @Email(message = EMAIL_VALIDATION_MESSAGE) String newEmail)
    throws BadRequestException{

        if(isCredentialTaken(newEmail))
            throw new BadRequestException("Email: " + newEmail + " is already registered.");

        var user = fetchUser(userId);

        updateCredential(
                user,
                newEmail,
                user::setEmail,
                User::getEmail
        );
    }



    private <T> void updateCredential(
            User user,
            String newCredential,
            Consumer<String> newCredentialFunction,
            Function<User, String> currentCredentialFunc){

        String currentCredential = currentCredentialFunc.apply(user);

        if(newCredential.equals(currentCredential)) {
            throw new BadRequestException("New credential is identical to the current one. ");
        }

        newCredentialFunction.accept(newCredential);
        userRepository.save(user);
    }


    private User fetchUser(UUID userId){
        return userRepository
                .findById(userId)
                .orElseThrow(()->
                        new NotFoundException("User with ID " + userId + " not found. "));
    }

    private boolean isCredentialTaken(String usernameOrEmail){
        return userRepository
                .findByUsernameOrEmail(usernameOrEmail)
                .isPresent();
    }

}

