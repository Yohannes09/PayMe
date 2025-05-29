package com.payme.authentication.service.auth;

import com.payme.authentication.dto.authentication.AuthenticationResponse;
import com.payme.authentication.dto.authentication.LoginRequest;
import com.payme.authentication.dto.authentication.RegististrationRequest;

public interface AuthenticationService {
    AuthenticationResponse login(LoginRequest loginRequest);
    void register(RegististrationRequest regististrationRequest);
    void logout(String token);
}
