package com.payme.tests;

import com.payme.app.entity.User;

import java.util.List;
import java.util.UUID;

public class MockedUsers {
    private static final List<User> MOCKED_USERS = List.of(
            User.builder()
                    .userId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                    .username("JohnDoe")
                    .email("johndoe@example.com")
                    .password("hashedpassword123")
                    .firstName("John")
                    .lastName("Doe")
                    .build(),
            User.builder()
                    .userId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                    .username("JaneDoe")
                    .email("janedoe@example.com")
                    .password("hashedpassword456")
                    .firstName("Jane")
                    .lastName("Doe")
                    .build(),
            User.builder()
                    .userId(UUID.fromString("33333333-3333-3333-3333-333333333333"))
                    .username("AliceSmith")
                    .email("alicesmith@example.com")
                    .password("hashedpassword789")
                    .firstName("Alice")
                    .lastName("Smith")
                    .build(),
            User.builder()
                    .userId(UUID.fromString("44444444-4444-4444-4444-444444444444"))
                    .username("BobJohnson")
                    .email("bobjohnson@example.com")
                    .password("hashedpassword101")
                    .firstName("Bob")
                    .lastName("Johnson")
                    .build(),
            User.builder()
                    .userId(UUID.fromString("55555555-5555-5555-5555-555555555555"))
                    .username("CharlieBrown")
                    .email("charliebrown@example.com")
                    .password("hashedpassword112")
                    .firstName("Charlie")
                    .lastName("Brown")
                    .build()
    );

    public List<User> getMockedUsers() {
        return MOCKED_USERS;
    }
}
