package com.payme.authentication.service;

import com.payme.authentication.constant.ValidationPattern;
import com.payme.authentication.entity.SecurityUser;
import com.payme.authentication.exception.BadRequestException;
import com.payme.authentication.exception.CredentialUpdateException;
import com.payme.authentication.exception.SecurityUserNotFoundException;
import com.payme.authentication.repository.SecurityUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Slf4j
@Lazy
@RequiredArgsConstructor
public class CredentialService {
    private final static String ERROR_MESSAGE = "FAILED CREDENTIAL UPDATE:";

    private final SecurityUserRepository securityUserRepository;
    private final PasswordEncoder passwordEncoder;



    @Transactional
    public void updateUsername(UUID id, String newUsername) throws BadRequestException {
        if(securityUserRepository.existsByUsernameIgnoreCase(newUsername)) {
            log.warn("{} provided existing username. ", ERROR_MESSAGE);
            throw new CredentialUpdateException("Username: " + newUsername + " taken. ");
        }

        SecurityUser securityUser = findSecurityUserById(id);
        validateCredential(
                securityUser,
                newUsername,
                SecurityUser::getUsername,
                ValidationPattern.USERNAME_PATTERN,
                ValidationPattern.USERNAME_VALIDATION_MESSAGE
        );

        updateCredential(
                securityUser,
                newUsername,
                securityUser::setUsername
        );

        log.info("User with ID {} successfully updated username. ", id);
    }

    @Transactional
    public void updateEmail(UUID id, String newEmail) throws BadRequestException{
        if(securityUserRepository.existsByEmailIgnoreCase(newEmail)) {
            log.warn("{} provided an existing email. ", ERROR_MESSAGE);
            throw new CredentialUpdateException("Email: " + newEmail + " taken. ");
        }

        SecurityUser securityUser = findSecurityUserById(id);

        validateCredential(
                securityUser,
                newEmail,
                SecurityUser::getEmail,
                ValidationPattern.EMAIL_PATTERN,
                ValidationPattern.EMAIL_VALIDATION_MESSAGE
        );

        updateCredential(
                securityUser,
                newEmail,
                securityUser::setEmail
        );

        log.info("User with ID {} successfully updated email. ", id);
    }

    @Transactional
    public void updatePassword(UUID id, String newPassword) throws BadRequestException{
        SecurityUser securityUser = findSecurityUserById(id);
        validateCredential(
                securityUser,
                newPassword,
                SecurityUser::getPassword,
                ValidationPattern.PASSWORD_PATTERN,
                ValidationPattern.PASSWORD_VALIDATION_MESSAGE
        );

        String encodedPassword = passwordEncoder.encode(newPassword);
        updateCredential(
                securityUser,
                encodedPassword,
                securityUser::setPassword
        );

        log.info("User with ID {} successfully updated password. ", id);
    }


    private SecurityUser findSecurityUserById(UUID id){
        return securityUserRepository.findById(id)
                .orElseThrow(() -> new SecurityUserNotFoundException("User not found ID: " + id));
    }

    // Remove this method. Client sends DTO that will validate what they want to change.
    //
    private void validateCredential(
            SecurityUser securityUser,
            String newCredential,
            Function<SecurityUser, String> currentCredentialFunction,
            String validationPattern,
            String validationPatternMessage
    ){
        if (newCredential == null){
            log.warn("{} provided a null new credential. ", ERROR_MESSAGE);
            throw new BadRequestException("User's new credential is null. ");
        }

        if(!validationPattern.matches(newCredential)){
            log.warn("{} new credential did not match pattern: {} ", ERROR_MESSAGE, validationPattern);
            throw new CredentialUpdateException(validationPatternMessage);
        }

        // Check if user isn't re-using the credential they're changing.
        String currentCredential = currentCredentialFunction.apply(securityUser);
        if(newCredential.equals(currentCredential)) {
            log.warn("{} New credential is identical to the current one.", ERROR_MESSAGE);
            throw new CredentialUpdateException("New credential is identical to the current one. ");
        }

    }

    // this is updating and persisting, naming is off.
    private <T> void updateCredential(
            SecurityUser securityUser,
            String newCredential,
            Consumer<String> newCredentialFunction
    ){
        newCredentialFunction.accept(newCredential);
        securityUserRepository.save(securityUser);
    }

}
