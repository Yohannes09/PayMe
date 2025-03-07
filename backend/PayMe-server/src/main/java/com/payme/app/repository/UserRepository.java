package com.payme.app.repository;

import com.payme.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

//    @Query(value = "SELECT user FROM User user " +
//            "WHERE LOWER(user.username) = LOWER(:usernameOrEmail)" +
//            "OR LOWER(user.email) = LOWER(:usernameOrEmail)")
    @Query(value = "SELECT * FROM payme_user " +
           "WHERE LOWER(username) = LOWER(:usernameOrEmail) " +
           "OR LOWER(email) = LOWER(:usernameOrEmail) " +
           "LIMIT 1 ", nativeQuery = true)
    Optional<User> findFirstByUsernameOrEmail(@Param(value = "usernameOrEmail") String usernameOrEmail);

}
