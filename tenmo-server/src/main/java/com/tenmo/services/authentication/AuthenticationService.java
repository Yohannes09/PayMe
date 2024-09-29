package com.tenmo.services.authentication;

import com.tenmo.dto.authentication.LoginDto;
import com.tenmo.dto.authentication.LoginResponseDto;
import com.tenmo.dto.authentication.RegisterUserDto;

public interface AuthenticationService {

    void register(RegisterUserDto registerDto);

    LoginResponseDto login(LoginDto loginDto);
}


//
//public AuthenticationControllerOld(TokenProvider tokenProvider,
//                                AuthenticationManagerBuilder authenticationManagerBuilder,
//                                UserRepository userRepository) {
//    this.tokenProvider = tokenProvider;
//    this.authenticationManagerBuilder = authenticationManagerBuilder;
//    this.userRepository = userRepository;
//}
//
//@PostMapping(path = "/login")
//public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
//
//    UsernamePasswordAuthenticationToken authenticationToken =
//            new UsernamePasswordAuthenticationToken(
//                    loginDto.getUsername(),
//                    loginDto.getPassword());
//
//    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//    String jwt = tokenProvider.createToken(authentication, false);
//
//    LOGGER.info("USER SIGN-IN ", new LoginResponseDto(jwt, userRepository.getUserByUsername(loginDto.getUsername())));
//
//    User user = null;
////        try {
////            user
////                    //= userRepository.getUserByUsername(loginDto.getUsername());
//    return new ResponseEntity<>(new LoginResponseDto(jwt, user), HttpStatus.CREATED);
////        } catch (DaoException e) {
////            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect.");
////        }
//
//}
//
//@ResponseStatus(HttpStatus.CREATED)
//@RequestMapping(path = "/register", method = RequestMethod.POST)
//public void register(@Valid @RequestBody RegisterUserDto newUser) {
////        try {
////            if (userRepository.getUserByUsername(newUser.getUsername()) != null) {
////                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists.");
////            } else {
////                userRepository.save(newUser);
////            }
////        }
////        catch (DaoException e) {
////            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User registration failed.");
////        }
//}