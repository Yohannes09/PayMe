package com.techelevator.tenmo.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

// Used to store different variables of a transfer, like the usernames.
public class TransferHistoryDto {
    @NotNull
    private int transferId;

    @NotNull
    private int senderAccountId;

    @NotNull
    private int recipientAccountId;

    @NotNull
    private String senderUsername;

    @NotNull
    private String recipientUsername;

    @Min(value = 0, message = "Enter amount greater than zero.")
    private double amount;

    public TransferHistoryDto(){

    }

    public TransferHistoryDto(
                        int transferId,
                        int senderAccountId,
                        int recipientAccountId,
                        String senderUsername,
                        String recipientUsername,
                        double amount) {

        this.transferId = transferId;
        this.senderAccountId = senderAccountId;
        this.recipientAccountId = recipientAccountId;
        this.senderUsername = senderUsername;
        this.recipientUsername = recipientUsername;
        this.amount = amount;
    }

    @NotNull
    public int getTransferId() {
        return transferId;
    }

    public @NotNull String getSenderUsername() {
        return senderUsername;
    }

    public @NotNull String getRecipientUsername() {
        return recipientUsername;
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

    public void setTransferId(@NotNull int transferId) {
        this.transferId = transferId;
    }

    public void setSenderAccountId(@NotNull int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public void setRecipientAccountId(@NotNull int recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }

    public void setSenderUsername(@NotNull String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setRecipientUsername(@NotNull String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public void setAmount(@Min(value = 0, message = "Enter amount greater than zero.") double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransferHistoryDto{" +
                "transferId=" + transferId +
                ", senderAccountId=" + senderAccountId +
                ", recipientAccountId=" + recipientAccountId +
                ", senderUsername='" + senderUsername + '\'' +
                ", recipientUsername='" + recipientUsername + '\'' +
                ", amount=" + amount +
                '}';
    }
}
