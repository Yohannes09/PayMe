package com.payme.authentication.service.auth;

import com.payme.authentication.dto.AuthenticationResponseDto;
import com.payme.authentication.dto.LoginDto;
import com.payme.authentication.dto.RegisterDto;

public interface AuthenticationService {
    AuthenticationResponseDto login(LoginDto loginDto);
    void register(RegisterDto registerDto);
}
