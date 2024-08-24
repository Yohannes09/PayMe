package com.techelevator.tenmo.model;

/*
* Will be used to store information from a transaction (server).
* This is due to SQL queries being printed to the console after each query.
* */
public class ClientTransfer {
    private final int transactionId;
    private final int senderId;
    private final String senderUsername;
    private final int recipientId;
    private final String recipientUsername;
    private final int transferStatusId;
    private final int transferTypeId;
    private final double amount;

    public ClientTransfer(int transactionId, int senderId, String senderUsername, int recipientId,
                          String recipientUsername, int transferStatusId, int transferTypeId, double amount) {
        this.transactionId = transactionId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.recipientId = recipientId;
        this.recipientUsername = recipientUsername;
        this.transferStatusId = transferStatusId;
        this.transferTypeId = transferTypeId;
        this.amount = amount;
    }


    public int getTransactionId() {
        return transactionId;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public double getAmount() {
        return amount;
    }

}
