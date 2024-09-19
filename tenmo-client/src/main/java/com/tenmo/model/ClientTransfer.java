package com.tenmo.model;

/*
* Used to map a transfer response from a REST API,
* to a Java object.
*
* Naming convention is a result of java being unable to recognize between
* Client and server models. */

public class ClientTransfer {
    private final int transactionId;
    private final int senderId;
    private final int recipientId;
    private final int transferStatusId;
    private final int transferTypeId;
    private final double amount;

    public ClientTransfer(int transactionId, int senderId, int recipientId, int transferStatusId, int transferTypeId, double amount) {
        this.transactionId = transactionId;
        this.senderId = senderId;
        this.recipientId = recipientId;
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

    public int getRecipientId() {
        return recipientId;
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
