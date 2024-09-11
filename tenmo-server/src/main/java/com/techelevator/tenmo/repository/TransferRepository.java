package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("INSERT INTO " +
            "transfer(transferTypeId, transferStatusId, accountFrom, accountTo, transferAmount, transferMessage) " +
            "VALUES(:transferTypeId, :transferStatusId, :accountFrom, :accountTo, :transferAmount, :transferMessage)")
    Transfer processTransfer(
            Integer transferTypeId,
            Integer transferStatusId,
            Long accountFrom,
            Long accountTo,
            BigDecimal transferAmount,
            String transferMessage);


    @Query("SELECT t FROM transfer t " +
            "WHERE t.transferId = :transferId")
    Transfer findTransferById(Long transferId);
    List<Transfer> getTransfers();

    /** View an account's transfer history */
    List<TransferResponseDto> getTransferHistory(Long accountId);

    /** View the status of requested transfers*/
    List<TransferResponseDto> accountTransferStatus(Integer transferStatusId, Long accountId);

    @Modifying  // tells jpa data is being modified
    @Query("UPDATE transfer t " +
            "SET t.transferStatusId = :newTransferStatusId " +
            "WHERE t.transferId = :transferId") // the ':' indicates the values will be passed at runtime using the method parameters
    Transfer updateTransferStatus(@Param("transferId") Long transferId,
                                  @Param("transferStatusId") Integer newTransferStatusId);

}
