package com.payme.authentication.service.auth;

import com.payme.authentication.dto.authentication.AuthenticationResponse;
import com.payme.authentication.dto.authentication.LoginRequest;
import com.payme.authentication.dto.authentication.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse login(LoginRequest loginRequest);
    void register(RegisterRequest registerRequest);
    void logout(String token);
}
