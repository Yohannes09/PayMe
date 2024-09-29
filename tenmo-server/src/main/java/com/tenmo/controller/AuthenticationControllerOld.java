package com.tenmo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to authenticate users.
 */
@RestController
public class AuthenticationControllerOld {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationControllerOld.class);

//    private final TokenProvider tokenProvider;
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
//    private final UserRepository userRepository;

//    public AuthenticationControllerOld(TokenProvider tokenProvider,
//                                    AuthenticationManagerBuilder authenticationManagerBuilder,
//                                    UserRepository userRepository) {
//        this.tokenProvider = tokenProvider;
//        this.authenticationManagerBuilder = authenticationManagerBuilder;
//        this.userRepository = userRepository;
//    }

//    @PostMapping(path = "/login")
//    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(
//                        loginDto.getUsernameOrEmail(),
//                        loginDto.getPassword());
//
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = tokenProvider.createToken(authentication, false);
//
//        LOGGER.info("USER SIGN-IN ", new LoginResponseDto(jwt, userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail())));
//
//        User user = null;
//        try {
//            user
//                    //= userRepository.getUserByUsername(loginDto.getUsername());
//            return new ResponseEntity<>(new LoginResponseDto(jwt, user), HttpStatus.CREATED);
//        } catch (DaoException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect.");
//        }
//        return null;
//    }

//    @ResponseStatus(HttpStatus.CREATED)
//    @RequestMapping(path = "/register", method = RequestMethod.POST)
//    public void register(@Valid @RequestBody RegisterUserDto newUser) {
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
//    }

}

