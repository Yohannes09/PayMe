package com.tenmo.controller;

import com.tenmo.dto.LoginDto;
import com.tenmo.dto.LoginResponseDto;
import com.tenmo.dto.RegisterUserDto;
import com.tenmo.security.jwt.TokenProvider;
import com.tenmo.entity.User;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.tenmo.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller to authenticate users.
 */
@RestController
public class AuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;

    public AuthenticationController(TokenProvider tokenProvider,
                                    AuthenticationManagerBuilder authenticationManagerBuilder,
                                    UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);

        LOGGER.info("USER SIGN-IN ", new LoginResponseDto(jwt, userRepository.getUserByUsername(loginDto.getUsername())));

        User user = null;
//        try {
//            user
//                    //= userRepository.getUserByUsername(loginDto.getUsername());
            return new ResponseEntity<>(new LoginResponseDto(jwt, user), HttpStatus.CREATED);
//        } catch (DaoException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect.");
//        }

    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public void register(@Valid @RequestBody RegisterUserDto newUser) {
//        try {
//            if (userRepository.getUserByUsername(newUser.getUsername()) != null) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists.");
//            } else {
//                userRepository.save(newUser);
//            }
//        }
//        catch (DaoException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User registration failed.");
//        }
    }

}

