package com.payme.authentication.configuration.notprod;

import com.payme.authentication.component.RoleProvider;
import com.payme.authentication.entity.Role;
import com.payme.authentication.entity.User;
import com.payme.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Configuration
@Profile("dev")
@Slf4j(topic = "DEFAULT_USER_GENERATOR")
public class DefaultUserGenerator {

    @Bean
    public CommandLineRunner createDefaultUsers(UserRepository userRepository, RoleProvider roleProvider){
        return args -> {
            Role adminRole = roleProvider.findRole("ADMIN");
            Role userRole = roleProvider.findRole("USER");

            User adminUser = createUser("admin12", "Admin12@", "admin@example.com", Set.of(adminRole));
            User regularUser = createUser("user12", "User123@", "user@example.com", Set.of(userRole));

            userRepository.saveAll(List.of(adminUser, regularUser));
            log.info("Default Admin and User accounts created");
        };
    }


    private User createUser(String username, String password, String email, Set<Role> roles){
        return User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email(email)
                .password(password)
                .roles(roles)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

}
