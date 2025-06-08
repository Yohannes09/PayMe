package com.payme.authentication.component;

import com.payme.authentication.component.util.Mapper;
import com.payme.authentication.dto.UserDto;
import com.payme.authentication.entity.Role;
import com.payme.authentication.entity.User;
import com.payme.authentication.exception.DuplicateCredentialException;
import com.payme.authentication.exception.UserNotFoundException;
import com.payme.authentication.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAccountManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //* @return the created {@link User} entity
    /**
     * Creates and persists a new {@link User} with the provided credentials and roles.
     * <p>
     * This method encodes the user's password, initializes account status flags,
     * and checks for duplicate username or email entries before saving.
     * </p>
     *
     * @param username the desired username for the new user; must be unique
     * @param email the user's email address; must be unique
     * @param password the raw password to be encoded and stored securely
     * @param roles a set of roles to assign to the new user
     * @throws DuplicateCredentialException if the username or email already exists
     */
    public void createNewUser(String username, String email, String password, Set<Role> roles){

        if(userRepository.existsByUsernameOrEmail(username, email)){
            throw new DuplicateCredentialException("Username or Email already registered. ");
        }

        userRepository.save(
                User.builder()
                        .username(username)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .roles(roles)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .build()
        );

    }


    @Cacheable(cacheNames = "user", key = "#userId")
    public UserDto findById(@NotNull UUID userId){
        return userRepository
                .findById(userId)
                .map(Mapper::entityToDto)
                .orElseThrow(()-> new UserNotFoundException("User not found: " + userId));
    }


    @Cacheable(cacheNames = "user", key = "#usernameOrEmail")
    public UserDto findByUsernameOrEmail(@NotBlank String usernameOrEmail){
        return userRepository
                .findByUsernameOrEmail(usernameOrEmail)
                .map(Mapper::entityToDto)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + usernameOrEmail));
    }


    public User findEntityById(@NotNull UUID userId){
        return userRepository
                .findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found: " + userId));
    }

    public User persistUserUpdate(User user){
        if (user.getId() == null)
            throw new RuntimeException("A user must be persisted before being updated. ");

        return userRepository.save(user);
    }


    // Provides encapsulation and allows for future enhancements. (caching, logging, etc.)
    public boolean existsByUsername(String username){
        if(username.isBlank())
            throw new NullPointerException("Provided null/empty username parameter to existsByUsername()");

        return userRepository.existsByUsernameIgnoreCase(username);
    }


    public boolean existsByEmail(@NotBlank String email){
        return userRepository.existsByEmailIgnoreCase(email);
    }

}
