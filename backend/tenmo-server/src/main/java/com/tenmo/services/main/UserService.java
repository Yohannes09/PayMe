package com.tenmo.services.main;

import com.tenmo.dto.user.UserDto;
import com.tenmo.exception.BadRequestException;

import java.util.UUID;

public interface UserService {
    UserDto findById(UUID userId);
}
