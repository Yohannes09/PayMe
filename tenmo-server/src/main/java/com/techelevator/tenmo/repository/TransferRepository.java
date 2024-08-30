package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.dto.TransferHistoryDto;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferRepository {
    //C
    Optional<Transfer> processTransfer(
            int transferTypeId, int transferStatusId, int senderAccountId, int recipientAccountId, double amount);

    //R
    Optional<Transfer> getTransferById(int transferId);
    List<Transfer> getTransfers();

    /** View an account's transfer history */
    List<TransferHistoryDto> getTransferHistory(int accountId);

    /** View the status of requested transfers*/
    List<TransferHistoryDto> accountTransferStatus(int transferStatusId, int accountId);

    //U
    Optional<Transfer> updateTransferStatus(int transferId, int newTransferStatusId);

    //D
    int deleteTransferById(int transferId);
}
