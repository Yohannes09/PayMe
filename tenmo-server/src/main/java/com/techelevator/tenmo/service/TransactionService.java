package com.techelevator.tenmo.service;

public interface TransactionService {
    boolean transferFunds(int senderId,
                          int recipientId,
                          double amountTransfered
                          );
}
