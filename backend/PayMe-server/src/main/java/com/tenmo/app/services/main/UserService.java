package com.tenmo.app.services.main;

import com.tenmo.app.dto.user.UserDto;

import java.util.UUID;

public interface UserService {
    UserDto findById(UUID userId);
}
