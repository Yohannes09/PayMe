package com.payme.tests.repository;

import com.payme.app.PaymeApplication;
import com.payme.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.payme.tests.MockedUsers;

@Slf4j
@SpringBootTest(classes = PaymeApplication.class)
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        var mockedUsers = new MockedUsers();

        log.info("Beginning setup process.");
        userRepository.saveAll(mockedUsers.getMockedUsers());
        log.info("Setup complete. ");
    }

    @AfterEach
    void rollBack(){
        log.info("Beginning  rollback. ");
        userRepository.deleteAll();
        log.info("Rollback complete. ");
    }

    @Test
    void testFindByUsernameOrEmail(){
        assertTrue(userRepository.findByUsernameOrEmail("JohnDoe").isPresent());
        assertTrue(userRepository.findByUsernameOrEmail("JohnDoe@example.com").isPresent());
    }

    @Test
    void testFindByUsernameOrEmailInvalidUser(){
        assertFalse(userRepository.findByUsernameOrEmail("Arthur").isPresent());
    }

    @Test
    void testIsCredentialTaken(){
        log.info("Running isCredentialTaken() test ");
        // Email is different but username is taken.
        assertTrue(userRepository.isCredentialTaken(
                "Johndoe", "spring@example.com")
        );

        assertFalse(userRepository.isCredentialTaken(
                "Java", "java@example.com")
        );
        log.info("isCredentialTaken() test completed ");
    }

}
