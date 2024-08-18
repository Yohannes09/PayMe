package com.techelevator.tenmo.dao;

public interface TransactionDao {
    boolean transferFunds(int senderAmount, int receiverAmount, double balance);
}
