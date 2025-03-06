package com.payme.app.authentication.configuration;

import com.payme.app.authentication.service.JwtService;
import com.payme.app.repository.UserRepository;
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

@RequiredArgsConstructor
@Component
public class ApplicationConfig {
    private final UserRepository userRepository;


    /**
     * Defines a {@link UserDetailsService} bean that retrieves user details for authentication.
     * <p>
     * This method returns a lambda function that implements the {@code UserDetailsService} interface,
     * which is a functional interface containing a single method:
     * </p>
     *
     * <pre>
     * UserDetails loadUserByUsername(String username);
     * </pre>
     *
     * <p>
     * When Spring Security calls {@code loadUserByUsername(username)}, it provides the {@code username} argument.
     * The lambda function then searches for the user by username or email in the database.
     * If no user is found, a {@link UsernameNotFoundException} is thrown.
     * </p>
     *
     * @return a {@code UserDetailsService} implementation that retrieves users from the database
     * @throws UsernameNotFoundException if the user is not found in the repository
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository
                .findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found. "));
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


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService){
        return new JwtAuthenticationFilter(jwtService, userDetailsService());
    }


    /**
     * Defines a UserDetailsService bean that retrieves user details for authentication.
     * It searches for a user by username or email in the database.
     * If the user is not found, it throws a UsernameNotFoundException.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
