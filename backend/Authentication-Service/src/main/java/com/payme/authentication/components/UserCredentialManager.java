package com.payme.authentication.components;

import com.payme.authentication.dto.credentialupdate.EmailUpdateRequest;
import com.payme.authentication.dto.credentialupdate.PasswordUpdateRequest;
import com.payme.authentication.dto.credentialupdate.UsernameUpdateRequest;
import com.payme.authentication.entity.User;
import com.payme.authentication.exception.BadRequestException;
import com.payme.authentication.exception.CredentialUpdateException;
import com.payme.authentication.exception.UserNotFoundException;
import com.payme.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Component
@Slf4j
@Lazy
@RequiredArgsConstructor
public class UserCredentialManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void updateUsername(UsernameUpdateRequest usernameUpdateRequest) throws BadRequestException {
        User user = fetchUserById(usernameUpdateRequest.id());
        String newUsername = usernameUpdateRequest.newUsername();

        validateCredential(user.getUsername(), newUsername, userRepository::existsByUsernameIgnoreCase);
        persistCredentialChange(user, newUsername, user::setUsername);

        log.info("User {} successfully updated username. ", user.getId());
    }

    @Transactional
    public void updateEmail(EmailUpdateRequest emailUpdateRequest) throws BadRequestException{
        User user = fetchUserById(emailUpdateRequest.id());
        String newEmail = emailUpdateRequest.newEmail();

        validateCredential(user.getEmail(), newEmail, userRepository::existsByEmailIgnoreCase);
        persistCredentialChange(user, newEmail, user::setEmail);

        log.info("User {} successfully updated email. ", user.getId());
    }

    @Transactional
    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest) throws BadRequestException{
        User user = fetchUserById(passwordUpdateRequest.id());
        String encodedPassword = passwordEncoder.encode(passwordUpdateRequest.newPassword());

        validateCredential(user.getPassword(), encodedPassword, null);
        persistCredentialChange(user, encodedPassword, user::setPassword);

        log.info("User {} successfully updated password. ", user.getId());
    }


    private User fetchUserById(UUID id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    private void validateCredential(
            String currentCredential,
            String newCredential,
            Predicate<String> uniquenessCheck
    ){
        if(currentCredential.equals(newCredential)){
            throw new CredentialUpdateException("New credential cannot be the same as the current one. ");
        }

        if(uniquenessCheck != null && uniquenessCheck.test(newCredential)){
            throw new CredentialUpdateException(newCredential + " is already taken.");
        }

    }

    private void persistCredentialChange(
            User user,
            String credential,
            Consumer<String> setNewCredential
    ){
        setNewCredential.accept(credential);
        userRepository.save(user);
    }

}
