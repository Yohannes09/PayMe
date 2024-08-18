package com.techelevator.tenmo.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transfer {
    // private static final Logger log = LoggerFactory.getLogger(Transfer.class);
    private final int transferId;
    private final int typeTypeId;
    private final int transferStatusId;
    private final int actSenderId;
    private final int actRecipientId;
    private final double amount;

    public Transfer(int transferId,
                    int typeTypeId,
                    int transferStatusId,
                    int actSenderId,
                    int actRecipientId,
                    double amount) {
        this.transferId = transferId;
        this.typeTypeId = typeTypeId;
        this.transferStatusId = transferStatusId;
        this.actSenderId = actSenderId;
        this.actRecipientId = actRecipientId;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
    }

    public int getTypeTypeId() {
        return typeTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public int getActSenderId() {
        return actSenderId;
    }

    public int getActRecipientId() {
        return actRecipientId;
    }
}
