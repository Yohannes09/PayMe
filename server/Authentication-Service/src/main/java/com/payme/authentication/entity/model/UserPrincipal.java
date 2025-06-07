package com.payme.authentication.entity.model;

import com.payme.authentication.dto.UserDto;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Builder
public record UserPrincipal(
        UUID id,
        String username,
        String password,
        String email,
        Set<String> roles,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        boolean enabled
) implements UserDetails {

    public static UserPrincipal dtoToPrincipal(UserDto user){
        return UserPrincipal.builder()
                .id(user.id())
                .username(user.username())
                .email(user.email())
                .password(user.password())
                .roles(user.roles())
                .accountNonExpired(user.accountNonExpired())
                .enabled(user.enabled())
                .accountNonLocked(user.accountNonLocked())
                .credentialsNonExpired(user.credentialsNonExpired())
                .build();
    }


    /**
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }

    /**
     * @return
     */
    @Override
    public String getPassword() {
        return password;
    }


    /**
     * @return
     */
    @Override
    public String getUsername() {
        return username;
    }


    /**
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }


    /**
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }


    /**
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }


    /**
     * @return
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
