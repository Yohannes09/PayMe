package com.payme.authentication.repository;

import com.payme.authentication.entity.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SecurityUserRepository extends JpaRepository<SecurityUser, UUID> {

    @Query("""
            SELECT user FROM SecurityUser user
            WHERE LOWER(user.username) = LOWER(:usernameOrEmail)
            OR LOWER(user.email) = LOWER(:usernameOrEmail)
            """)
    Optional<SecurityUser> findByUsernameOrEmail(
            @Param("usernameOrEmail") String usernameOrEmail
    );

    @Query("""
            SELECT CASE WHEN COUNT(user) > 0 THEN true ELSE false END
            FROM SecurityUser user
            WHERE (LOWER(user.username) = LOWER(:username))
            OR LOWER(user.email) = LOWER(:email)
            """)
    boolean existsByUsernameOrEmail(
            @Param("username") String username,
            @Param("email") String email
    );

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);
}
