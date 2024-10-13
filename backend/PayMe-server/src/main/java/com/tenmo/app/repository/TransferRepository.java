package com.tenmo.app.repository;

import com.tenmo.app.dto.transfer.TransferResponseDto;
import com.tenmo.app.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
//import org.hibernate.
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    @Query(value = "SELECT * FROM transfer tr " +
            "JOIN transfer_type tt ON tt.transfer_type_id = tr.transfer_type_id " +
            "JOIN transfer_status ts ON ts.transfer_status_id = tr.transfer_status_id " +
            "WHERE LOWER(tt.transfer_type_desc) = 'request' " +
            "AND LOWER(ts.transfer_status_desc) = 'approved' ", nativeQuery = true)
    List<Transfer> getApprovedRequests();


    @Modifying
    @Query(value = "UPDATE transfer t " +
            "SET t.transferStatusId = :newTransferStatusId " +
            "WHERE t.transferId = :transferId", nativeQuery = true)
    Optional<Transfer> updateTransferStatus(@Param("transferId") Long transferId,
                                  @Param("newTransferStatusId") Integer newTransferStatusId
    );

    @Query(value = "SELECT " +
            "tr.transfer_id AS transferId, " +
            "sender.username AS accountFromUsername, " +
            "recipient.username AS accountToUsername, " +
            "tr.amount, " +
            "tr.transfer_message AS transferMessage, " +
            "tr.currency, " +
            "tr.created_at AS createdAt, " +
            "tt.transfer_type_desc AS transferTypeDescription," +
            "ts.transfer_status_desc AS transferStatusDescription," +
            "tt.transfer_type_id AS typeId," +
            "ts.transfer_status_id AS statusId" +
            "FROM transfer tr " +
            "JOIN account sender_ac ON sender_ac.account_id = tr.account_from " +
            "JOIN tenmo_user sender ON sender.user_id = sender_ac.user_id " +
            "JOIN account recipient_ac ON recipient_ac.account_id = tr.account_to " +
            "JOIN tenmo_user recipient ON recipient.user_id = recipient_ac.user_id " +
            "JOIN transfer_type tt ON tt.transfer_type_id = tr.transfer_type_id " +
            "JOIN transfer_status ts ON ts.transfer_status_id = tr.transfer_status_id" +
            "WHERE tr.transfer_id IN (:transferIds) ", nativeQuery = true)
    Optional<TransferResponseDto> transferResponse(@Param("transferIds") UUID transferIds);

    @Query(value = "SELECT " +
            "tr.transfer_id AS transferId, " +
            "sender.username AS accountFromUsername, " +
            "recipient.username AS accountToUsername, " +
            "tr.amount, " +
            "tr.transfer_message AS transferMessage, " +
            "tr.currency, " +
            "tr.created_at AS createdAt, " +
            "tt.transfer_type_desc AS transferTypeDescription," +
            "ts.transfer_status_desc AS transferStatusDescription," +
            "tt.transfer_type_id AS typeId," +
            "ts.transfer_status_id AS statusId" +
            "FROM transfer tr " +
            "JOIN account sender_ac ON sender_ac.account_id = tr.account_from " +
            "JOIN tenmo_user sender ON sender.user_id = sender_ac.user_id " +
            "JOIN account recipient_ac ON recipient_ac.account_id = tr.account_to " +
            "JOIN tenmo_user recipient ON recipient.user_id = recipient_ac.user_id " +
            "JOIN transfer_type tt ON tt.transfer_type_id = tr.transfer_type_id " +
            "JOIN transfer_status ts ON ts.transfer_status_id = tr.transfer_status_id" +
            "WHERE tr.transfer_id IN (:transferIds) ", nativeQuery = true)
    List<TransferResponseDto> transferResponse(@Param("transferIds") List<UUID> transferIds);
}
