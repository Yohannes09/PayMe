package com.payme.authentication.service;

import com.payme.authentication.constant.ValidationPattern;
import com.payme.authentication.dto.UserDto;
import com.payme.authentication.entity.User;
import com.payme.authentication.exception.BadRequestException;
import com.payme.authentication.exception.UserNotFoundException;
import com.payme.authentication.mapper.UserMapper;
import com.payme.authentication.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Transactional
@Validated
@Service
@AllArgsConstructor
public class UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto findById(UUID userId) {
        return userRepository.findById(userId)
                .map(UserMapper::mapUserToDto)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found. "));
    }

    public void updateUsername(UUID userId, String newUsername) throws BadRequestException {
        credentialMatchesPattern(newUsername, ValidationPattern.USERNAME_PATTERN);
        if(userRepository.existsByUsernameIgnoreCase(newUsername)) {
            throw new BadRequestException("Username: " + newUsername + " taken. ");
        }

        User user = fetchUser(userId);
        updateCredential(user, newUsername, user::setUsername, User::getUsername);
    }


    public void updatePassword(UUID userId, String newPassword) throws BadRequestException{
        credentialMatchesPattern(newPassword, ValidationPattern.PASSWORD_PATTERN);
        User user = fetchUser(userId);
        String encodedPassword = passwordEncoder.encode(newPassword);

        updateCredential(user, encodedPassword, user::setPassword, User::getPassword);
    }


    public void updateEmail(UUID userId, String newEmail) throws BadRequestException{
        credentialMatchesPattern(newEmail, ValidationPattern.EMAIL_PATTERN);

        if(userRepository.existsByEmailIgnoreCase(newEmail)) {
            throw new BadRequestException("Email: " + newEmail + " is already registered.");
        }

        User user = fetchUser(userId);
        updateCredential(user, newEmail, user::setEmail, User::getEmail);
    }

    private void credentialMatchesPattern(String newCredential, String validationPattern){
        if (newCredential == null || !validationPattern.matches(newCredential)){
            throw new BadRequestException("");
        }
    }

    // *** Extract the current credential (e.g., username or password) from the user object ***
    // `currentCredentialFunc` is a Function<User, String>, meaning it takes a User and returns a String
    // Calling `apply(user)` invokes the function, retrieving the credential from the user
    // Example: If `currentCredentialFunc` is `User::getUsername`, this is equivalent to `user.getUsername()`
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
        log.info("User fetched with ID: {}", userId);
        return userRepository
                .findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with ID " + userId + " not found. "));
    }

}


