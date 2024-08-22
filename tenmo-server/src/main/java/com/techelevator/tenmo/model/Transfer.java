package com.techelevator.tenmo.model;

public class Transfer {
    private final int transferId;
    private final int typeId;
    private final int transferStatusId;
    private final int senderAccountId;
    private final int recipientAccountId;
    private final double amount;

    public Transfer(int transferId,
                    int typeId,
                    int transferStatusId,
                    int senderAccountId,
                    int recipientAccountId,
                    double amount) {
        this.transferId = transferId;
        this.typeId = typeId;
        this.transferStatusId = transferStatusId;
        this.senderAccountId = senderAccountId;
        this.recipientAccountId = recipientAccountId;
        this.amount = amount;
    }


    public int getTransferId() {
        return transferId;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public int getRecipientAccountId() {
        return recipientAccountId;
    }

    public double getAmount(){return amount;}
}
