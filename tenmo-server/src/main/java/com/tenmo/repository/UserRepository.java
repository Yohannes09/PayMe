package com.tenmo.repository;

import com.tenmo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //NON-NATIVE SQL QUERY
    @Query(value = "SELECT user FROM User user " +
            "WHERE LOWER(user.username) = LOWER(:usernameOrEmail)" +
            "OR LOWER(user.email) = LOWER(:usernameOrEmail) ")
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);
    // Update {username, password, email}
}
