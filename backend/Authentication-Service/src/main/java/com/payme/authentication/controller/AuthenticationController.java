package com.payme.authentication.controller;

import com.payme.authentication.dto.authentication.LoginRequest;
import com.payme.authentication.dto.authentication.RegisterRequest;
import com.payme.authentication.service.auth.AuthenticationService;
import com.payme.authentication.dto.authentication.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5500"})
@RequestMapping("/v1/auth")
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    public AuthenticationController(
            @Qualifier("JwtAuthenticationService") AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }


    @PostMapping(path = "/login")
    @Operation(
            summary = "Authenticate user and get access and refresh tokens. ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully authenticated. ",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - bad credentials"),
                    @ApiResponse(responseCode = "404", description = "Couldn't find user. ")
            }
    )
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        log.info("New login by user: {}", loginRequest.usernameOrEmail());
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping(path = "/register")
    @Operation(
            summary = "Register a new user. ",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully registered. ",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(responseCode = "", description = "Unauthorized - bad credentials")
            }
    )
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest registerRequest){
        log.info("New registration request received. ");
        authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
