package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.dto.TransferHistoryDto;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferRepository {
    //  USEFUL
    Optional<Transfer> proccessTransfer(
                                        int transferTypeId,
                                        int transferStatusId,
                                        int senderAccountId,
                                        int recipientAccountId,
                                        double amount);

    Optional<Transfer> getTransferById(int id);

    List<Transfer> getTransfers();

    int deleteTransferById(int transferId);

    List<TransferHistoryDto> getTransferHistory(int accountId);

    Optional<Transfer> updateTransferStatus(int transferId, int newTransferStatusId);


    // Hasn't been though out, but use these to get transactions by their type or status.

    // My idea, combine both into 1. Ie, you can view different status on either sent or requested transfers.
    List<TransferHistoryDto> accountTransferTypeHistory();

    List<TransferHistoryDto> accountTransferStatusHistory();

}
