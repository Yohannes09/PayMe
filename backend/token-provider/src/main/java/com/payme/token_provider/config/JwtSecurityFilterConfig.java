package com.payme.token_provider.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtSecurityFilterConfig {

    private static final List<String> WHITE_LISTED_ADDRESSES = List.of(
            "",
            ""
    );

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/api/v1/public-key").permitAll()

                                // White list of IPS
                                .anyRequest().access((authentication, context) -> {
                                    String incomingRequestIPAddress = context.getRequest().getRemoteAddr();

                                    if(isWhiteListedAddress(incomingRequestIPAddress)){
                                        return new AuthorizationDecision(true);
                                    }
                                    return new AuthorizationDecision(
                                            authentication.get().isAuthenticated()
                                    );
                                })

                                //.anyRequest().authenticated()
                )
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private boolean isWhiteListedAddress(String ipAddress){
        return WHITE_LISTED_ADDRESSES.stream()
                .anyMatch(address -> address.equals(ipAddress));
    }

}
