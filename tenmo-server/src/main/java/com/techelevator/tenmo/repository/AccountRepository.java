package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  Repository for managing account entities.
 * <ul>
 *   <li></li>
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


    @Query(value = "SELECT " +
            "tr.transfer_id AS transferId, " +
            "sender.username AS accountFromUsername, " +
            "recipient.username AS accountToUsername, " +
            "tr.amount, " +
            "tr.transfer_message AS transferMessage, " +
            "tr.currency, " +
            "tr.created_at AS createdAt, " +
            "tt.transfer_type_desc AS transferTypeDescription," +
            "ts.transfer_status_desc AS transferStatusDescription" +
            "FROM transfer tr " +
            "JOIN account sender_ac ON sender_ac.account_id = tr.account_from " +
            "JOIN tenmo_user sender ON sender.user_id = sender_ac.user_id " +
            "JOIN account recipient_ac ON recipient_ac.account_id = tr.account_to " +
            "JOIN tenmo_user recipient ON recipient.user_id = recipient_ac.user_id " +
            "JOIN transfer_type tt ON tt.transfer_type_id = tr.transfer_type_id " +
            "JOIN transfer_status ts ON ts.transfer_status_id = tr.transfer_status_id" +
            "WHERE tr.account_from = :accountId OR tr.account_to = :accountId"
            , nativeQuery = true)
    List<TransferResponseDto> getAccountTransferHistory(@Param("accountId") Long accountId);

    @Query(value = "SELECT ac FROM account ac WHERE account_id IN :accountIds")
    List<Account> findAccountsById(@Param("accountsIds") List<Long> accountIds);

}
