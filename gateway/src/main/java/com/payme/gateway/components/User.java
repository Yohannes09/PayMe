package com.payme.gateway.components;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Component
public class User implements UserDetails {
    private UUID userId;
    private String username;
    private String password;
    private List<GrantedAuthority> grantedAuthorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialIsNonExpired;
    private boolean enabled;


    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialIsNonExpired() {
        return credentialIsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
