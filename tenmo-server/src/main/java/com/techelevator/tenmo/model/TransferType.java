package com.techelevator.tenmo.model;

public class TransferType {
    private final int transferTypeId;
    private final String description;

    public TransferType(int transferTypeId, String description) {
        this.transferTypeId = transferTypeId;
        this.description = description;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public String getDescription() {
        return description;
    }
}
