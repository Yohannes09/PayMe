package com.techelevator.tenmo.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TransferDto {
    @NotNull
    private final int senderAccountId;
    @NotNull
    private final int recipientAccountId;
    @Min(value = 0, message = "Enter amount greater than zero.")
    private final double amount;

    public TransferDto(int senderAccountId,
                       int actRecipientId,
                       double amount) {

        this.senderAccountId = senderAccountId;
        this.recipientAccountId = actRecipientId;
        this.amount = amount;
    }


    @NotNull
    public int getSenderAccountId() {
        return senderAccountId;
    }

    @NotNull
    public int getRecipientAccountId() {
        return recipientAccountId;
    }

    @Min(value = 0, message = "Enter amount greater than zero.")
    public double getAmount() {
        return amount;
    }
}
//int transferId,
//int typeTypeId,
//int transferStatusId,

// this.transferId = transferId;
// this.typeTypeId = typeTypeId;
// this.transferStatusId = transferStatusId;
//    @NotNull
////    public int getTransferId() {
////        return transferId;
////    }
////
////    @NotNull
////    public int getTypeTypeId() {
////        return typeTypeId;
////    }
////
////    @NotNull
////    public int getTransferStatusId() {
////        return transferStatusId;
////    }