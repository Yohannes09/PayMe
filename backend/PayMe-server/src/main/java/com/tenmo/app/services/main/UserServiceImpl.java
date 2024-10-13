package com.tenmo.app.services.main;

import com.tenmo.app.dto.user.UserDto;
import com.tenmo.app.exception.NotFoundException;
import com.tenmo.app.mapper.UserMapper;
import com.tenmo.app.repository.UserRepository;
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


