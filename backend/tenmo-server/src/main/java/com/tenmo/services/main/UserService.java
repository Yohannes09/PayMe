package com.tenmo.services.main;

import com.tenmo.dto.user.UserDto;
import com.tenmo.exception.BadRequestException;

import java.util.UUID;

public interface UserService {

    UserDto findById(UUID userId);

    void updateUsername(UUID userId, String newUsername) throws BadRequestException;

    void updatePassword(UUID userId, String newPassword) throws BadRequestException;

    void updateEmail(UUID userId, String newEmail) throws BadRequestException;
}
