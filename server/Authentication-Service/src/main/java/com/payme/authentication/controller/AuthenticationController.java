package com.payme.authentication.controller;

import com.payme.authentication.constant.Endpoints;
import com.payme.authentication.dto.authentication.LoginRequest;
import com.payme.authentication.dto.authentication.RegististrationRequest;
import com.payme.authentication.service.auth.AuthenticationService;
import com.payme.authentication.dto.authentication.AuthenticationResponse;
import com.payme.authentication.service.auth.CookieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
        log.info("New login by user: {}", loginRequest.usernameOrEmail());

        AuthenticationResponse  authResponse = authenticationService.login(loginRequest);
        cookieService.setTokenCookies(
                authResponse.refreshToken(),
                authResponse.accessToken(),
                servletResponse
        );

        return ResponseEntity.ok(authResponse.trimmed());
    }


    @PostMapping(path = Endpoints.Auth.REGISTER)
    @Operation(
            summary = "Create a new user. ",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully registered. ",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))
            }
    )
    public ResponseEntity<Void> register(@Valid @RequestBody RegististrationRequest regististrationRequest){
        log.info("New registration request received. ");
        authenticationService.register(regististrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping(path = Endpoints.Auth.TOKEN_REFRESH)
    @Operation(
            summary = "Issue new access and refresh tokens. Valid refresh token must come attached to request."
    )
    public ResponseEntity<?> refresh(){
        return ResponseEntity.ok(authenticationService.refresh(null));
    }


    @PostMapping(path = Endpoints.Auth.LOGOUT)
    @Operation(
            summary = "Terminate the user's authenticated session by black-listing the refresh token. "
    )
    public ResponseEntity<Void> logout(){
        authenticationService.logout("");

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
