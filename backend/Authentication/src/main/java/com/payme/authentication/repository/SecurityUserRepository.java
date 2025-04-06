package com.payme.authentication.repository;

import com.payme.authentication.entity.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SecurityUserRepository extends JpaRepository<SecurityUser, UUID> {
    Optional<SecurityUser> findByUsernameOrEmail(String usernameOrEmail);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    @Query(value =
            "SELECT CASE WHEN COUNT(user) > 0 THEN true ELSE false END " +
            "FROM User user " +
            "WHERE (LOWER(user.username) = LOWER(:username)) " +
            "OR LOWER(user.email) = LOWER(:email) "
    )
    boolean isCredentialTaken(String username, String email);
}
