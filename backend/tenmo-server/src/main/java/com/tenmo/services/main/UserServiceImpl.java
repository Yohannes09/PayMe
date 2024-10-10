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

}


