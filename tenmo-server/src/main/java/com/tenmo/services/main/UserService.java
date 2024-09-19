package com.tenmo.services.main;

import com.tenmo.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long userId);

    Optional<User> createNewUser(String username, String passwordHash, String email, String role);

    // NEEDS: update {username, password, email}
}
