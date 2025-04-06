package com.payme.authentication.service;

import com.payme.authentication.dto.RegisterDto;
import com.payme.authentication.entity.SecurityUser;
import com.payme.authentication.exception.SecurityUserNotFoundException;
import com.payme.authentication.repository.SecurityUserRepository;
import com.payme.authentication.user_micro_serv.dto.UserDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class SecurityUserService {
    private final SecurityUserRepository securityUserRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityUser createUser(RegisterDto registerDto){
        return SecurityUser.builder()
                .username(registerDto.username())
                .email(registerDto.email())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(false)
                .build();
    }

    // map to dto and return a dto
    public SecurityUser findById(UUID userId){
        log.info("User fetched with ID: {}", userId);
        return securityUserRepository
                .findById(userId)
                .orElseThrow(()-> new SecurityUserNotFoundException("User with ID " + userId + " not found. "));
    }

    public SecurityUser save(SecurityUser securityUser) {
        return null;
    }

    public boolean isCredentialTaken(String username, String email) {
        return false;
    }
}
