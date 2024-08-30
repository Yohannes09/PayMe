package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dto.TransferHistoryDto;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferService {
    Optional<Transfer> processTransfer(int transferTypeId, int senderAccountId, int recipientAccountId, double amount);


    // Obtain a list of an accounts transfer history.
    List<TransferHistoryDto> getAccountHistory(int accountId);
    Optional<Transfer> getTransferById(int transferId);
    List<TransferHistoryDto> accountTransferStatus(int transferStatusId, int accountId);

    Optional<Transfer> updatePendingTransfer(int transferId, int newTransferStatusId);
}
