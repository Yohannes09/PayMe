package com.techelevator.tenmo.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
* Fields being final caused issues when being deserialized
*
* TransferDto was created to hide sensitive info like transfer ID.
* */

public class TransferDto {

    @NotNull
    private int transferId;
    @NotNull
    private int senderAccountId;
    @NotNull
    private int recipientAccountId;
    @NotNull
    private int transferStatusId;
    @NotNull
    private int transferTypeId;
    @Min(value = 0, message = "Enter amount greater than zero.")
    private double amount;


    public TransferDto(
                    int transferId,
                    int senderAccountId,
                    int recipientAccounttId,
                    int transferStatusId,
                    int transferTypeId,
                    double amount) {

        this.transferId = transferId;
        this.senderAccountId = senderAccountId;
        this.recipientAccountId = recipientAccounttId;
        this.transferStatusId = transferStatusId;
        this.transferTypeId = transferTypeId;
        this.amount = amount;
    }

    public TransferDto(){

    }

    public void setTransferId(int transferId){
        this.transferId = transferId;
    }

    public int getTransferId(){
        return transferId;
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

    public void setSenderAccountId(@NotNull int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public void setRecipientAccountId(@NotNull int recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }

    public void setAmount(@Min(value = 0, message = "Enter amount greater than zero.") double amount) {
        this.amount = amount;
    }

    @NotNull
    public int getTransferStatusId() {
        return transferStatusId;
    }

    @NotNull
    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferStatusId(@NotNull int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public void setTransferTypeId(@NotNull int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

}
