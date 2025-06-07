package com.payme.authentication.configuration.notprod;

import com.payme.authentication.component.RoleProvider;
import com.payme.authentication.component.UserAccountManager;
import com.payme.authentication.dto.UserDto;
import com.payme.authentication.entity.Role;
import com.payme.authentication.entity.User;
import com.payme.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Set;

@Configuration
@Profile("dev")
@Lazy
@Slf4j(topic = "DEFAULT_USER_GENERATOR")
public class DefaultUserGenerator {

    @Bean
    public CommandLineRunner createDefaultUsers(UserAccountManager userAccountManager, UserRepository userRepository, RoleProvider roleProvider){
        return args -> {
            log.info("Creating default Admin and User accounts.");

            Role adminRole = roleProvider.findRole("ADMIN");
            Role userRole = roleProvider.findRole("USER");

            userAccountManager.createNewUser("admin12", "admin@example.com","Admin12@", Set.of(adminRole));
            userAccountManager.createNewUser("user12", "user@example.com", "User123@", Set.of(userRole));

//            log.info("Before searching for user. ");
//            UserDto savedAdmin = userAccountManager.findByUsernameOrEmail("admin12");
//            UserDto savedUser = userAccountManager.findByUsernameOrEmail("user12");
//
//            savedAdmin.getRoles().add(adminRole);
//            savedUser.getRoles().add(userRole);
//
//            userRepository.saveAll(List.of(savedUser, savedAdmin));

            log.info("Default Admin and User accounts created");
        };
    }


    private User createUser(String username, String password, String email){//, Set<Role> roles){
        return User.builder()
                //.id(UUID.randomUUID())
                .username(username)
                .email(email)
                .password(password)
                //.roles(roles)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

}
