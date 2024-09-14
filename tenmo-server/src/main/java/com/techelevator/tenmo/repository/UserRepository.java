package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT user FROM tenmo_user user " +
            "WHERE LOWER(username) = LOWER(:username); ",
            nativeQuery = true)
    User getUserByUsername(String username);
    // Update {username, password, email}
}
