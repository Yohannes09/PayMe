package com.payme.authentication.controller;

import com.payme.authentication.dto.LoginDto;
import com.payme.authentication.service.AuthenticationService;
import com.payme.authentication.dto.AuthenticationResponseDto;
import com.payme.authentication.dto.RegisterDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5500"})
@RequestMapping("/api/v1/auth")
@RestController
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Value("${application.security.jwt.secret}")
    private String jwtSecret;

    @Value("${application.security.gateway.api-key}")
    private String gatewayApiKey;


    public AuthenticationController(
            @Qualifier("JwtAuthenticationService") AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }


    // Endpoint that exposes Jwt secret-key if provided a valid API-key.
    // For now, keeping this simple.
    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey(@RequestHeader("api-key") String apiKey){
        if(!gatewayApiKey.equals(apiKey)){
            return ResponseEntity.status(403).body("FORBIDDEN");
        }
        log.info("Gateway successfully retrieved backend jwt secret. ");
        return ResponseEntity.ok(jwtSecret);
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
