package com.payme.authentication.service.auth;

import com.payme.authentication.dto.authentication.AuthenticationResponse;
import com.payme.authentication.dto.authentication.LoginRequest;
import com.payme.authentication.dto.authentication.RegististrationRequest;

import java.util.UUID;

public interface AuthenticationService {
    AuthenticationResponse login(LoginRequest loginRequest);

    void register(RegististrationRequest regististrationRequest);

    AuthenticationResponse refresh(UUID id);

    void logout(String token);
}
