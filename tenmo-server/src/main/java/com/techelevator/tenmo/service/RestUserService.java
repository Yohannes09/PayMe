package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repository.JdbcUserRepository;
import com.techelevator.tenmo.repository.UserRepository;

import java.util.Optional;

public class RestUserService implements UserService{
    private final UserRepository userRepository;

    public RestUserService(){
        this.userRepository = new JdbcUserRepository();
    }

    public RestUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Optional<User> getUserById(int userId) {
        return Optional.ofNullable(userRepository.getUserById(userId));
    }
}
