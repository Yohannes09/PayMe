package com.tenmo.services.main;

import com.tenmo.dto.user.UserDto;
import com.tenmo.entity.User;
import com.tenmo.exception.NotFoundException;
import com.tenmo.mapper.UserMapper;
import com.tenmo.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.NonNull;
import com.tenmo.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Transactional
@Validated
@Service
public class UserServiceImpl implements UserService{
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{5,15}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[0-9!@#$%^&*]).{6,}$";

    private static final String USERNAME_VALIDATION_MESSAGE = "Username should be 5-15 characters long with no special symbols.";
    private static final String PASSWORD_VALIDATION_MESSAGE = "Password must be 6+ characters long, have at least 1 capital letter, and a number or special character.";
    private static final String EMAIL_VALIDATION_MESSAGE = "Entered invalid email";

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDto findById(UUID userId) {
        return userRepository.findById(userId)
                .map(UserMapper::mapUserToDto)
                .orElseThrow(() -> new NotFoundException(""));
    }


    // Refactor service eventually. A lot of repeated steps.
    @Override
    public void updateUsername(UUID userId, @NonNull @Pattern(regexp = USERNAME_PATTERN
            , message = USERNAME_VALIDATION_MESSAGE) String newUsername) throws BadRequestException{

        if(isUsernameTaken(newUsername))
            throw new BadRequestException("Username: " + newUsername + " taken. ");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Could not find user with ID: " + userId));

        if(user.getUsername().equals(newUsername))
            throw new BadRequestException("Current username is identical to new username");

        user.setUsername(newUsername);

        userRepository.save(user);
    }


    @Override
    public void updatePassword(UUID userId, @NonNull @Pattern
            (regexp = PASSWORD_PATTERN, message = PASSWORD_VALIDATION_MESSAGE) String newPassword)
    throws BadRequestException{

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Could not find user with ID: " + userId));

        if(user.getUsername().equals(newPassword))
            throw new BadRequestException("Current passoword is identical to new password");

        user.setPassword(newPassword);

        userRepository.save(user);
    }



    public void updateEmail(UUID userId, @NonNull @Email
            (message = EMAIL_VALIDATION_MESSAGE) String newEmail) throws BadRequestException{

        if(isEmailTaken(newEmail))
            throw new BadRequestException("Email: " + newEmail + " is already registered.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Could not find user with ID: " + userId));

        if(user.getUsername().equals(newEmail))
            throw new BadRequestException("Current email is identical to new email");
        user.setUsername(newEmail);

        userRepository.save(user);
    }



    private boolean isUsernameTaken(String username) {
        return userRepository.findByUsernameOrEmail(username).isPresent();
    }

    private boolean isEmailTaken(String email) {
        return userRepository.findByUsernameOrEmail(email).isPresent();
    }

}


