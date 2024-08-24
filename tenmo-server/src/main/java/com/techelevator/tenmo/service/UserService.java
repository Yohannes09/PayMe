package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(int userId);
}
