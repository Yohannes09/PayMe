package com.payme.common.entity;

import com.payme.common.model.BaseUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payme_user")
@Entity
public class User implements BaseUser {

    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @NotNull
    @Column(nullable = false, name = "first_name")
    private String firstName;

    @NotNull
    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Size(max = 20, min = 4)
    @NotNull
    @Column(unique = true, nullable = false)
    private String username;

    @NotNull
    @Size(min = 6)
    private String password;

    @Email
    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key for User
            inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key for Role
    )
    private Set<Role> roles;

    @Column(nullable = false)
    private boolean active;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Account> accounts = new ArrayList<>(3);

    @Override
    public UUID getId(){
        return userId;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

}
