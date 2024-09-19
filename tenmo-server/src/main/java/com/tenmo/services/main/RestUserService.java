package com.tenmo.services.main;

import com.tenmo.entity.User;
import com.tenmo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestUserService implements UserService{
    private final UserRepository userRepository;

    public RestUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> createNewUser(String username, String passwordHash, String email, String role) {
        return Optional.empty();
    }
}
