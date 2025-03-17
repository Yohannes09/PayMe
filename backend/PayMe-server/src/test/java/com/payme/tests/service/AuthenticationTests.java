package com.payme.tests.service;

import com.payme.app.PaymeApplication;
import com.payme.app.authentication.TokenRepository;
import com.payme.app.authentication.dto.LoginDto;
import com.payme.app.authentication.dto.RegisterDto;
import com.payme.app.authentication.entity.SessionToken;
import com.payme.app.authentication.service.AuthenticationService;
import com.payme.app.authentication.service.JwtService;
import com.payme.app.entity.User;
import com.payme.app.exception.UserNotFoundException;
import com.payme.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PaymeApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationTests {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private TokenRepository tokenRepository;

    @BeforeEach
    public void setUp(){

    }

    @Test
    void testRegister() {
        RegisterDto dto = new RegisterDto("John", "Doe", "testUser", "test@example.com", "password");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("mockToken");

        authenticationService.register(dto);

        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenRepository, times(1)).save(any(SessionToken.class));
    }

    @Test
    void testInvalidCredentialsLogin(){
        LoginDto loginDto = new LoginDto("JohnDoe", "password");

        when(userRepository.findByUsernameOrEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authenticationService.login(loginDto));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsernameOrEmail(anyString());
        verify(tokenRepository, times(0)).save(any(SessionToken.class));
    }


    void testValidCredentialsLogin(){
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(jwtService.generateToken(any(User.class))).thenReturn("mockToken");
    }

}
