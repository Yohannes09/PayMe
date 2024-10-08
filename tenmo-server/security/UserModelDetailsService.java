package com.tenmo.security;

import com.tenmo.exception.NotFoundException;
import com.tenmo.exception.UserNotActivatedException;
import com.tenmo.repository.UserRepository;
import com.tenmo.security.model.Authority;
import com.tenmo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Authenticate a user from the database.
 */

@Component("userDetailsService")
public class UserModelDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserModelDetailsService.class);

    private final UserRepository userRepository;

    public UserModelDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating user '{}'", login);
        return createSpringSecurityUser(login,
                userRepository.findByUsernameOrEmail(login)
                        .orElseThrow(() -> new NotFoundException("User " + login + " not found. ")));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String login, User user) {
        if (!user.isActive()) {
            throw new UserNotActivatedException("User " + login + " was not activated");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        Set<Authority> userAuthorities = //Set.of(new Authority(user.getRole()));
//
//        for (Authority authority : userAuthorities) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getRole()));
//        }
        String userRole = "ROLE_" + user.getRole().toUpperCase();
        grantedAuthorities.add(new SimpleGrantedAuthority(userRole));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
}

