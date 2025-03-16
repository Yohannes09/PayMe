package com.payme.gateway.controller;

import com.payme.gateway.dto.AuthenticationResponseDto;
import com.payme.gateway.dto.LoginDto;
import com.payme.gateway.service.AuthenticationRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class ClientRequestController {
    private final AuthenticationRequestService authenticationRequestService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(LoginDto loginDto){
        AuthenticationResponseDto authResponse = authenticationRequestService.submitLoginCredentials(loginDto);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
