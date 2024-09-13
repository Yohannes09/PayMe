package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query(value = "INSERT INTO " +
            "transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount, transfer_message) " +
            "VALUES(:transferTypeId, :transferStatusId, :accountFrom, :accountTo, :amount, :transferMessage)",
    nativeQuery = true)
    Transfer processTransfer(
            @Param("transferTypeId") Integer transferTypeId,
            @Param("transferStatusId") Integer transferStatusId,
            @Param("accountFrom") Long accountFrom,
            @Param("accountTo") Long accountTo,
            @Param("amount") BigDecimal amount,
            @Param("transferMessage") String transferMessage);


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
            "WHERE tr.transfer_id = :transferId",
        nativeQuery = true)
    TransferResponseDto getDetailedTransfer(@Param("transferId") Long transferId);


    @Query(value = "SELECT * FROM transfer tr " +
            "WHERE tr.transfer_id = :transferId", nativeQuery = true)
    Transfer findTransferById(Long transferId);


    /** View an account's transfer history */
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
    List<TransferResponseDto> getTransferHistory(@Param("accountId") Long accountId);

    /** View the status of requested transfers*/
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
            "WHERE (tr.account_from = :accountId OR tr.account_to = :accountId) " +
            "AND tr.transfer_status_id = :transferStatusId ",
            nativeQuery = true)
    List<TransferResponseDto> accountTransferStatus(@Param("transferStatusId") Integer transferStatusId,
                                                    @Param("accountId") Long accountId);

    @Modifying  // tells jpa data is being modified
    @Query(value = "UPDATE transfer t " +
            "SET t.transferStatusId = :newTransferStatusId " +
            "WHERE t.transferId = :transferId",
            nativeQuery = true) // the ':' indicates the values will be passed at runtime using the method parameters
    Transfer updateTransferStatus(@Param("transferId") Long transferId,
                                  @Param("newTransferStatusId") Integer newTransferStatusId);

}
