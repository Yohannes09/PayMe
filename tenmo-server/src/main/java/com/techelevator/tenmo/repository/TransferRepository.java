package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferRepository {

    Optional<Transfer> proccessTransfer(int senderId,
                                        int recipientId,
                                        int transferStatus,
                                        int transferType,
                                        double amount);

    List<Transfer> accountTransferHistory(int id);

    Optional<Transfer> getTransferById(int id);

    List<Transfer> getTransfers();

    int deleteTransfer(int transferId);

    List<Transfer> getPendingRequests(int accountId);
}
