package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dto.TransferHistoryDto;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferService {

    Optional<Transfer> getTransferById(int transferId);

    // Obtain a list of an accounts transfer history.
    List<TransferHistoryDto> getAccountHistory(int accountId);

    //
    Optional<Transfer> processTransfer(
            int transferTypeId, int senderAccountId, int recipientAccountId, double amount);

    Optional<Transfer> acceptTransfer(int transferId);

    Optional<Transfer> declineTransfer(int transferId);
}
