package com.payme.authentication.service.auth;

import com.payme.authentication.dto.authentication.AuthenticationResponse;
import com.payme.authentication.dto.authentication.LoginRequest;
import com.payme.authentication.dto.authentication.RegististrationRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("OAuthAuthenticationService")
public class OAuthAuthenticationService implements AuthenticationService {
    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public void register(RegististrationRequest regististrationRequest) {

    }

    @Override
    public void logout(String token) {

    }

}
