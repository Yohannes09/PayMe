package com.techelevator.tenmo.security.repository;

import com.techelevator.tenmo.dto.RegisterUserDto;
import com.techelevator.tenmo.security.model.User;

import java.util.List;

public interface UserRepository {

    List<User> getUsers();

    User getUserById(int id);

    User getUserByUsername(String username);

    User createUser(RegisterUserDto user);
}
