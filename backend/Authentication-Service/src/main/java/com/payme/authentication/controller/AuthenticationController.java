package com.payme.authentication.controller;

import com.payme.authentication.dto.LoginDto;
import com.payme.authentication.service.auth.AuthenticationService;
import com.payme.authentication.dto.AuthenticationResponseDto;
import com.payme.authentication.dto.RegisterDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5500"})
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;



    public AuthenticationController(
            @Qualifier("JwtAuthenticationService") AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }


    @PostMapping(path = "/login")
    public ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody LoginDto loginDto){
        log.info("New login by user: {}", loginDto.usernameOrEmail());
        return ResponseEntity.ok(authenticationService.login(loginDto));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto registerDto){
        log.info("New registration request received. ");
        authenticationService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
