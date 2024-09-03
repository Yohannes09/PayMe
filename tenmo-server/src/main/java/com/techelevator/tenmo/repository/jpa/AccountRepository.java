package com.techelevator.tenmo.repository.jpa;

import com.techelevator.tenmo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  <p>
 *      - Extending <b>JpaRepository</b> interface allows for full CRUD operation
 *      functionality.
 *      <br>
 *      - No need to create a class and provide the implementations.
 *  </p>*/
public interface AccountRepository extends JpaRepository<Account, Long> {
    // If more functionality is needed, define in the child interface.
}
