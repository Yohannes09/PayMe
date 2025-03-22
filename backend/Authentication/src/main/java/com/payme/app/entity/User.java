package com.payme.app.entity;

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
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@Table(name = "payme_user")
@Entity
public class User implements UserDetails {

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
//, cascade = CascadeType.ALL
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().toString()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword(){
        return this.password;
    }
}

/**
 *
 * Lesson(s) learned
 *      User has a many-to-many relationship with Role. Previous
 *      implementation of User had one-to-many relationship
 *      with Role.
 *
 *      THE PROBLEM?
 *      Each role belonged to one user. This made no sense as
 *      the roles USER, ADMIN, ETC should be shared across many
 *      users.
 *
 *      */