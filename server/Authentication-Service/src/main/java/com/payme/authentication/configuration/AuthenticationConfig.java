package com.payme.authentication.configuration;

import com.payme.authentication.component.UserAccountManager;
import com.payme.authentication.dto.UserDto;
import com.payme.authentication.entity.model.UserPrincipal;
import com.payme.authentication.repository.UserRepository;
import com.payme.authentication.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthenticationConfig {
    private final UserAccountManager userAccountManager;

    /**
     * Defines a {@link UserDetailsService} bean that retrieves user details for authentication.
     * <p>
     * This method returns a lambda function that implements the {@code UserDetailsService} functional interface.
     * </p>
     *
     * <p>
     * Calls {@code loadUserByUsername(username)}.
     *
     * @return a {@code UserDetailsService} implementation that retrieves users from the database
     * @throws UsernameNotFoundException if the user is not found in the repository
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return usernameOrEmail -> Optional.ofNullable(userAccountManager.findByUsernameOrEmail(usernameOrEmail))
                    .map(UserPrincipal::dtoToPrincipal)
                    .orElseThrow(() -> new UserNotFoundException(""));

    }



    /**
     * Defines an AuthenticationManager bean that orchestrates authentication processes.
     * It retrieves the authentication manager from the AuthenticationConfiguration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration)
            throws Exception {
        return authConfiguration.getAuthenticationManager();
    }


//    @Bean
//    public AuthenticationFilterConfig jwtAuthenticationFilter(){
//        return new AuthenticationFilterConfig(userDetailsService());
//    }


    /**
     * Defines a UserDetailsService bean that retrieves user details for authentication.
     * It searches for a user by username or email in the database.
     * If the user is not found, it throws a UsernameNotFoundException.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }


}
