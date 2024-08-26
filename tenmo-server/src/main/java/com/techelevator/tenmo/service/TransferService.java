package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferService {

    Optional<Transfer> requestTransfer(int senderAccountId, int recipientAccountId, double amount);

    void processTransferRequest(
            boolean accepted,
            int senderAccountId,
            int recipientAccountId,
            int transferId
    );

    Optional<Transfer> processTransfer(int sendAccountId, int recipientAccountId, double amount);

    List<Transfer> accountTransferHistory(int accountId);

    Optional<Transfer> getTransferById(int transferId);

    List<Transfer> getAccountTransferStatus(int accountId, int transferStatusId);

    void processDeposit(int accountId, double balance);

    void processWithdraw(int accountId, double balance);
}
