package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

/**
 *  <p>
 *      - Extending <b>JpaRepository</b> interface allows for full CRUD operation
 *      functionality.
 *      <br>
 *      - No need to create a class and provide the implementations.
 *  </p>*/
public interface AccountRepository extends JpaRepository<Account, Long> {
    // If more functionality is needed, define in the child interface.
    void updateBalance(Long accountId, BigDecimal amount);
}
