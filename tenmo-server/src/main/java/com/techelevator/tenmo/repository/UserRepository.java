package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
