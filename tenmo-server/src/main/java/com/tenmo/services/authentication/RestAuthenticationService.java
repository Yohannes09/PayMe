package com.tenmo.services.authentication;

import com.tenmo.dto.authentication.LoginDto;
import com.tenmo.dto.authentication.LoginResponseDto;
import com.tenmo.dto.authentication.RegisterUserDto;
import com.tenmo.entity.User;
import com.tenmo.exception.NotFoundException;
import com.tenmo.mapper.UserMapper;
import com.tenmo.repository.UserRepository;
import com.tenmo.security.jwt.TokenProvider;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RestAuthenticationService implements AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationService.class);

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RestAuthenticationService(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userRepository = userRepository;
    }


    @Override
    public void register(RegisterUserDto registerDto){

        try {
            if(userExists(registerDto.getEmail()))
                throw new BadRequestException("User already registered. ");
            else
                userRepository.save(UserMapper.mapRegisterDtoToUser(registerDto));
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }

    }

    @SneakyThrows
    @Override
    public LoginResponseDto login(LoginDto loginDto) {
        User user = userRepository
                .findByUsernameOrEmail(loginDto.getUsernameOrEmail())
                .orElseThrow(() -> new NotFoundException("Entered incorrect username or email. " + loginDto.getUsernameOrEmail()));

        if(!passwordEncoder.matches(user.getPasswordHash(), loginDto.getPassword()))
            throw new BadRequestException("INCORRECT CREDENTIALS ");
        LOGGER.info("USER SIGN-IN ", user.getUsername());

        String jwt = getLoginToken(loginDto);
        return new LoginResponseDto(jwt, UserMapper.mapUserToDto(user));
    }


    private String getLoginToken(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(),
                        loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.createToken(authentication, false);
    }

    private boolean userExists(String email){
        return userRepository.findByUsernameOrEmail(email).get() != null;
    }
}
