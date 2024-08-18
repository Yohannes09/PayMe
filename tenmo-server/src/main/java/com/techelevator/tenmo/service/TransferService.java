package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Transfer;

import java.util.Optional;

public interface TransferService {

    Optional<Transfer> processTransfer(int sendAccountId, int recipientAccountId, double amount);

    double getAccountIdBalance(int accountId);

    Optional<Transfer>  getTransferById(int id);
}
