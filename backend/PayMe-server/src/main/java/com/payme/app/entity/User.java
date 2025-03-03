package com.payme.app.entity;

import com.payme.app.constants.PaymeRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
//@Getter
//@Setter
@Table(name = "payme_user")
@Entity
public class User implements UserDetails {

    @Id
    @Column(name = "userId", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID userId;

    @NotNull
    @Column(nullable = false)
    private String firstName;

    @NotNull
    @Column(nullable = false)
    private String lastName;

    // NotNull is for User creation, nullable is to enforce no null in the column.
    @Size(max = 20, min = 4, message = "Enter a username 4-20 characters long.")
    @NotNull
    @Column(unique = true, nullable = false)
    private String username;

    @NotNull
    @Size(min = 6, message = "Password must be at least 6 characters long. ")
    private String password;

    @Email
    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    @ElementCollection(targetClass = PaymeRoles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "userId"))
    @Enumerated(EnumType.STRING)
    private Set<PaymeRoles> roles;

    @Column(nullable = false)
    private boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "payme_user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email;
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

//    public static void main(String[] args) {
//        var newUser = User.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .username("johndoe")
//                .password("<PASSWORD>")
//                .email("<EMAIL>")
//                .roles(Set.of(PaymeRoles.USER, PaymeRoles.ADMIN))
//                .isActive(true)
//                .build();
//
//        System.out.println(newUser.getUsername() + newUser.getRoles());
//    }
}
