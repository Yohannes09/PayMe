package com.payme.app.repository;

import com.payme.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value =
            "SELECT user FROM User user " +
            "WHERE (LOWER(user.username) = LOWER(:usernameOrEmail))" +
            "OR LOWER(user.email) = LOWER(:usernameOrEmail)")
    Optional<User> findByUsernameOrEmail(@Param(value = "usernameOrEmail") String usernameOrEmail);


    @Query(value =
            "SELECT CASE WHEN COUNT(user) > 0 THEN true ELSE false END " +
            "FROM User user " +
            "WHERE (LOWER(user.username) = LOWER(:username)) " +
            "OR LOWER(user.email) = LOWER(:email) ")
    boolean isCredentialTaken(@Param("username") String username, @Param("email") String email);


    boolean existsByUsernameIgnoreCase(String username);


    boolean existsByEmailIgnoreCase(String email);
}
