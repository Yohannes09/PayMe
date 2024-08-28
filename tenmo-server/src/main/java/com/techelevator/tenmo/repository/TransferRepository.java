package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferRepository {
    //  USEFUL
    Optional<Transfer> proccessTransfer(int senderId,
                                        int recipientId,
                                        int transferStatus,
                                        int transferType,
                                        double amount);

    Optional<Transfer> getTransferById(int id);

    List<Transfer> getTransfers();

    int deleteTransfer(int transferId);

    List<TransferResponseDto> getTransferHistoryTEST(int accountId); // not too sure yet

    //useless
    List<Transfer> accountTransferHistory(int id);
    List<Transfer> getAccountTransferStatus(int accountId, int transferStatusId);

}
