package com.payme.app.repository;

import com.payme.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "SELECT user FROM User user " +
            "WHERE LOWER(user.username) = LOWER(:usernameOrEmail)" +
            "OR LOWER(user.email) = LOWER(:usernameOrEmail) ")
    Optional<User> findByUsernameOrEmail(
            @Param(value = "usernameOrEmail") String usernameOrEmail);

}
