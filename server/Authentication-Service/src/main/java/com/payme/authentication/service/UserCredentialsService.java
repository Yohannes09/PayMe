package com.payme.authentication.service;

import com.payme.authentication.component.UserAccountManager;
import com.payme.authentication.dto.credentialupdate.EmailUpdateRequest;
import com.payme.authentication.dto.credentialupdate.PasswordUpdateRequest;
import com.payme.authentication.dto.credentialupdate.UsernameUpdateRequest;
import com.payme.authentication.entity.User;
import com.payme.authentication.exception.CredentialUpdateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
@Slf4j(topic = "USER_CREDENTIAL_SERVICE")
@Lazy
@RequiredArgsConstructor
public class UserCredentialsService {
    private final UserAccountManager userAccountManager;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void updateUsername(UsernameUpdateRequest usernameUpdateRequest){
        User user = userAccountManager.findById(usernameUpdateRequest.id());
        String newUsername = usernameUpdateRequest.newUsername();

        validateCredential(user.getUsername(), newUsername, userAccountManager::existsByUsername);
        persistCredentialChange(user, newUsername, user::setUsername);

        log.info("User {} successfully updated username. ", user.getId());
    }


    @Transactional
    public void updateEmail(EmailUpdateRequest emailUpdateRequest){
        User user = userAccountManager.findById(emailUpdateRequest.id());
        String newEmail = emailUpdateRequest.newEmail();

        validateCredential(user.getEmail(), newEmail, userAccountManager::existsByEmail);
        persistCredentialChange(user, newEmail, user::setEmail);

        log.info("User {} successfully updated email. ", user.getId());
    }


    @Transactional
    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest){
        User user = userAccountManager.findById(passwordUpdateRequest.id());
        String newPassword = passwordEncoder.encode(passwordUpdateRequest.newPassword());

        validateCredential(user.getPassword(), newPassword, null);
        persistCredentialChange(user, newPassword, user::setPassword);

        log.info("User {} successfully updated password. ", user.getId());
    }


    private void validateCredential(
            String currentCredential, String newCredential, Predicate<String> uniquenessCheck
    ){
        if(currentCredential.equals(newCredential)){
            throw new CredentialUpdateException("New credential cannot be the same as the current one. ");
        }

        if(uniquenessCheck != null && uniquenessCheck.test(newCredential)){
            throw new CredentialUpdateException(newCredential + " is already taken.");
        }

    }


    private void persistCredentialChange(
            User user, String credential, Consumer<String> setNewCredential
    ){
        setNewCredential.accept(credential);
        userAccountManager.persistUserUpdate(user);
    }

}
