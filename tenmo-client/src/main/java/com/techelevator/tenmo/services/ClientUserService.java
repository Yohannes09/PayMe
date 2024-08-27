package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;

import java.util.Optional;

public interface ClientUserService {

    Optional<User> getUserById(int userId);
}
