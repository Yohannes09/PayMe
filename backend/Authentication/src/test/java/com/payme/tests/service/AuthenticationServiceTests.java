package com.payme.tests.service;

import com.payme.app.PaymeApplication;
import com.payme.app.authentication.TokenRepository;
import com.payme.app.authentication.dto.LoginDto;
import com.payme.app.authentication.dto.RegisterDto;
import com.payme.app.authentication.entity.SessionToken;
import com.payme.app.authentication.service.AuthenticationService;
import com.payme.app.authentication.service.JwtService;
import com.payme.app.constants.PaymeRoles;
import com.payme.app.entity.Role;
import com.payme.app.entity.User;
import com.payme.app.exception.DuplicateCredentialException;
import com.payme.app.repository.RoleRepository;
import com.payme.app.repository.UserRepository;
import com.payme.tests.MockedUsers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest(classes = PaymeApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class AuthenticationServiceTests {

    @Autowired private AuthenticationService authenticationService;
    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private UserRepository userRepository;
    @MockBean private JwtService jwtService;
    @MockBean private BCryptPasswordEncoder passwordEncoder;
    @MockBean private TokenRepository tokenRepository;
    @MockBean private RoleRepository roleRepository;

    private MockedUsers mockedUsers = new MockedUsers();
    private RegisterDto dto = new RegisterDto("John", "Doe", "testUser", "test@example.com", "password");
    private LoginDto loginDto = new LoginDto("JohnDoe", "password");
    private User user = mockedUsers.getMockedUsers().get(0);


    @Test
    void testRegister() {
        Role mockRole = new Role(1L, PaymeRoles.USER);

        when(userRepository.isCredentialTaken(anyString(), anyString()))
                .thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(anyString()))
                .thenReturn("encodedPassword");
        when(roleRepository.findByRole(any(PaymeRoles.class)))
                .thenReturn(Optional.of(mockRole));

        authenticationService.register(dto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterWithDuplicateCredentials() {
        when(userRepository.isCredentialTaken(eq("testUser"), eq("test@example.com"))).thenThrow(DuplicateCredentialException.class);

        assertThrows(DuplicateCredentialException.class, () -> authenticationService.register(dto));

        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testInvalidCredentialsLogin() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Could not find user: " + loginDto.usernameOrEmail()));

        assertThrows(BadCredentialsException.class, () -> authenticationService.login(loginDto));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never())
                .findByUsernameOrEmail(anyString());
    }

    @Test
    void testValidCredentialsLogin(){
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, loginDto.password()));
        when(tokenRepository.findAllByUserId(any(UUID.class)))
                .thenReturn(new ArrayList<>());
        when(jwtService.generateToken(any(User.class)))
                .thenReturn("mockToken");

        authenticationService.login(loginDto);

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenRepository, times(1))
                .findAllByUserId(any(UUID.class));
        verify(tokenRepository, times(1))
                .save(any(SessionToken.class));
    }

}
