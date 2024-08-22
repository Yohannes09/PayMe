package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.List;
import java.util.Optional;

public interface TenmoService {

    Optional<Transfer> processTransfer(int sendAccountId, int recipientAccountId, double amount);

    Optional<Transfer> requestTransfer(int senderAccountId, int recipientAccountId, double amount);

    void processTransferRequest(
            boolean accepted,
            int senderAccountId,
            int recipientAccountId,
            int transferId
    );

    List<Transfer> accountTransferHistory(int accountId);

    Optional<Transfer> getTransferById(int transferId);

    List<Transfer> accountPendingTransfers(int accountId);

    void processDeposit(int accountId, double balance);

    void processWithdraw(int accountId, double balance);

    Optional<Account> getAccountById(int accountId);

    Optional<User> getUserById(int userId);
}
