package com.techelevator.tenmo.repository.authenticationRepo;

import com.techelevator.tenmo.dto.RegisterUserDto;
import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserRepository {

    List<User> getUsers();

    User getUserById(int id);

    User getUserByUsername(String username);

    User createUser(RegisterUserDto user);
}
