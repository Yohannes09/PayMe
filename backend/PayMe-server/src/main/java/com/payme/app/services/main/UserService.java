package com.payme.app.services.main;

import com.payme.app.dto.user.UserDto;

import java.util.UUID;

public interface UserService {
    UserDto findById(UUID userId);
}
