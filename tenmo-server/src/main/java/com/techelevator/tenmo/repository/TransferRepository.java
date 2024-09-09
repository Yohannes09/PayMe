package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("INSERT INTO " +
            "transfer(transferId, transferTypeId, transferStatusId, accountFrom, accountTo, transferAmount, transferMessage) " +
            "")
    Optional<Transfer> processTransfer(
            int transferTypeId, int transferStatusId, int senderAccountId, int recipientAccountId, double amount);


    Optional<Transfer> getTransferById(int transferId);
    List<Transfer> getTransfers();

    /** View an account's transfer history */
    List<TransferResponseDto> getTransferHistory(int accountId);

    /** View the status of requested transfers*/
    List<TransferResponseDto> accountTransferStatus(int transferStatusId, int accountId);

    @Modifying  // tells jpa data is being modified
    @Query("UPDATE transfer t " +
            "SET t.transferStatusId = :newTransferStatusId " +
            "WHERE t.transferId = :transferId") // the ':' indicates the values will be passed at runtime using the method parameters
    Optional<Transfer> updateTransferStatus(@Param("transferId") int transferId,
                                            @Param("transferStatusId") int newTransferStatusId);

}
