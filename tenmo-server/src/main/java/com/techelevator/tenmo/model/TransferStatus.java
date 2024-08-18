package com.techelevator.tenmo.model;

public class TransferStatus {
    private final int transferStatusId;
    private final String description;

    public TransferStatus(int transferStatusId, String description) {
        this.transferStatusId = transferStatusId;
        this.description = description;
    }


    public int getTransferStatusId() {
        return transferStatusId;
    }

    public String getDescription() {
        return description;
    }

}
