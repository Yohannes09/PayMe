package com.payme.authentication.controller;

import com.payme.authentication.constant.Endpoints;
import com.payme.authentication.dto.authentication.LoginRequest;
import com.payme.authentication.dto.authentication.RegistrationRequest;
import com.payme.authentication.service.auth.AuthenticationService;
import com.payme.authentication.dto.authentication.AuthenticationResponse;
import com.payme.authentication.service.auth.CookieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5500"})
@RequestMapping(Endpoints.Auth.BASE)
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final CookieService cookieService;

    public AuthenticationController(
            @Qualifier("jwtAuthenticationService") AuthenticationService authenticationService,
            CookieService cookieService
    ) {
        this.authenticationService = authenticationService;
        this.cookieService = cookieService;
    }


    @PostMapping(path = Endpoints.Auth.LOGIN)
    @Operation(
            summary = "Authenticate user credentials and issue access and refresh tokens. ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully authenticated. ",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - bad credentials"),
                    @ApiResponse(responseCode = "404", description = "Couldn't find user. ")
            }
    )
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse servletResponse
    ){
        log.info("New login attempt: {}", loginRequest.usernameOrEmail());
        AuthenticationResponse  authResponse = authenticationService.login(loginRequest);

        return buildAuthenticationResponse(authResponse, servletResponse);
    }


    @PostMapping(path = Endpoints.Auth.REGISTER)
    @Operation(
            summary = "Create a new user. ",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully registered. ")
            }
    )
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest registrationRequest){
        log.info("New registration request received. ");
        authenticationService.register(registrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping(path = Endpoints.Auth.TOKEN_REFRESH + "/{id}")
    @Operation(
            summary = "Issue new access and refresh tokens. Valid refresh token must come attached to request.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refresh successful. ",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))
            }
    )
    public ResponseEntity<AuthenticationResponse> refresh(
            @PathVariable Long id, HttpServletResponse servletResponse
    ){
        AuthenticationResponse authResponse = authenticationService.refresh(id);

        return buildAuthenticationResponse(authResponse, servletResponse);
    }


    @PostMapping(path = Endpoints.Auth.LOGOUT)
    @Operation(
            summary = "Terminate the user's authenticated session by black-listing the refresh token. ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refresh successful. ")
            }
    )
    public ResponseEntity<Void> logout(HttpServletRequest servletRequest){
        String refreshTokenCookie = cookieService.extractCookie(servletRequest, "refresh-token");
        authenticationService.logout(refreshTokenCookie);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    private ResponseEntity<AuthenticationResponse> buildAuthenticationResponse(
            AuthenticationResponse authResponse,
            HttpServletResponse servletResponse
    ){
        cookieService.setTokenCookies(
                authResponse.refreshToken(),
                authResponse.accessToken(),
                servletResponse
        );

        return ResponseEntity.ok(authResponse.trimmed());
    }

}
