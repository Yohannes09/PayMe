package com.payme.authentication.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserPrincipal implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Set<String> roles;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;


    /**
     * @return
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
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
