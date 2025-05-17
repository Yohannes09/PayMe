package com.payme.authentication.service.auth;

import com.payme.authentication.dto.AuthenticationResponseDto;
import com.payme.authentication.dto.LoginDto;
import com.payme.authentication.dto.RegisterDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("OAuthAuthenticationService")
public class OAuthAuthenticationService implements AuthenticationService {
    @Override
    public AuthenticationResponseDto login(LoginDto loginDto) {
        return null;
    }

    @Override
    public void register(RegisterDto registerDto) {

    }

    @Override
    public void logout(String token) {

    }

}
