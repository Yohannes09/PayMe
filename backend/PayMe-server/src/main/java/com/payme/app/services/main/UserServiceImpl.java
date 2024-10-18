package com.payme.app.services.main;

import com.payme.app.dto.user.UserDto;
import com.payme.app.exception.NotFoundException;
import com.payme.app.mapper.UserMapper;
import com.payme.app.repository.UserRepository;
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


