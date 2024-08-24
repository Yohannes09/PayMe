package com.techelevator.tenmo.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
* Fields being final caused issues when being deserialized
* */

public class TransferDto {
    @NotNull
    private int senderAccountId;
    @NotNull
    private int recipientAccountId;
    @Min(value = 0, message = "Enter amount greater than zero.")
    private double amount;

    public TransferDto(int senderAccountId,
                       int actRecipientId,
                       double amount) {

        this.senderAccountId = senderAccountId;
        this.recipientAccountId = actRecipientId;
        this.amount = amount;
    }

    public TransferDto(){

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
}
