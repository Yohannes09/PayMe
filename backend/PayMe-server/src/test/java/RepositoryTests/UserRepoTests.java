package RepositoryTests;

import com.payme.app.PaymeApplication;
import com.payme.app.entity.User;
import com.payme.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(classes = PaymeApplication.class)
@ActiveProfiles("test")
public class UserRepoTests {

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        log.info("Beginning setup process.");
        userRepository.saveAll(fetchMockedUsers());
        userRepository.findAll().stream().forEach(System.out::println);
        log.info("Setup complete. ");
    }

    @AfterEach
    void rollBack(){
        userRepository.deleteAll(fetchMockedUsers());
    }

    @Test
    void testFindByUsernameOrEmail(){
        assertTrue(userRepository.findByUsernameOrEmail("JohnDoe").isPresent());
        assertTrue(userRepository.findByUsernameOrEmail("JohnDoe@example.com").isPresent());
    }


    List<User> fetchMockedUsers(){
        User mockUser1 = User.builder()
                .userId(UUID.randomUUID())
                .username("JohnDoe")
                .email("johndoe@example.com")
                .password("hashedpassword123")
                .firstName("John")
                .lastName("Doe")
                .build();

        User mockUser2 = User.builder()
                .userId(UUID.randomUUID())
                .username("JaneDoe")
                .email("janedoe@example.com")
                .password("hashedpassword456")
                .firstName("Jane")
                .lastName("Doe")
                .build();
        User mockUser3 = User.builder()
                .userId(UUID.randomUUID())
                .username("AliceSmith")
                .email("alicesmith@example.com")
                .password("hashedpassword789")
                .firstName("Alice")
                .lastName("Smith")
                .build();

        User mockUser4 = User.builder()
                .userId(UUID.randomUUID())
                .username("BobJohnson")
                .email("bobjohnson@example.com")
                .password("hashedpassword101")
                .firstName("Bob")
                .lastName("Johnson")
                .build();

        User mockUser5 = User.builder()
                .userId(UUID.randomUUID())
                .username("CharlieBrown")
                .email("charliebrown@example.com")
                .password("hashedpassword112")
                .firstName("Charlie")
                .lastName("Brown")
                .build();

        return List.of(mockUser1, mockUser2, mockUser3, mockUser4, mockUser5);
    }
}
