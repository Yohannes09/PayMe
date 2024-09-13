package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Update {username, password, email}
}
