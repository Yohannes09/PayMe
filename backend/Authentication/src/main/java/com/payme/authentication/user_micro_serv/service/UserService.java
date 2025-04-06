package com.payme.authentication.user_micro_serv.service;

import com.payme.authentication.dto.RegisterDto;
import com.payme.authentication.exception.DuplicateCredentialException;
import com.payme.authentication.exception.SecurityUserNotFoundException;
import com.payme.authentication.user_micro_serv.dto.UserDto;
import com.payme.authentication.user_micro_serv.entity.User;
import com.payme.authentication.user_micro_serv.mapper.UserMapper;
import com.payme.authentication.user_micro_serv.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

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
                .orElseThrow(() -> new SecurityUserNotFoundException("User with ID: " + userId + " not found. "));
    }

    public void createUser(RegisterDto registerDto){
        if(userRepository.isCredentialTaken(registerDto.username(), registerDto.email())){
            throw new DuplicateCredentialException("Username or email already in use. ");
        }

    }


    private User fetchUser(UUID userId){
        log.info("User fetched with ID: {}", userId);
        return userRepository
                .findById(userId)
                .orElseThrow(()-> new SecurityUserNotFoundException("User with ID " + userId + " not found. "));
    }

}


