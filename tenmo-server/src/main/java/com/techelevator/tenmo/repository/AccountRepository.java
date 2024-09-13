package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * <p>
 *  Repository for managing account entities.
 * <ul>
 *   <li>Provides CRUD operations for account data.</li>
 *   <li>Handles account balance updates.</li>
 * </ul>
 * <p>
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Modifying
    @Query(value = "UPDATE account ac " +
            "SET ac.balance = ac.balance + :amount " +
            "WHERE ac.account_id = :accountId", nativeQuery = true)
    void updateBalance(@Param("accountId") Long accountId,
                       @Param("amount") BigDecimal amount);

}
