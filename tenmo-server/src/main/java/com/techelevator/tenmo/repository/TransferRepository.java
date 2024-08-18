package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferRepository {

    Optional<Transfer> createTransfer(int senderId,
                                      int recipientId,
                                      double amountTransfered,
                                      int transferStatus,
                                      int transferType,
                                      double amount);

    Optional<Transfer> getTransferById(int id);

    List<Transfer> getTransfers();

    int deleteTransfer(int transferId);

    //Optional<Transfer> findTransferById(int id);

}
