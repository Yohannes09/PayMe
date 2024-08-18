package com.techelevator.tenmo.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

//
public class TransferResponseDto {
    @NotNull
    private final int accountSenderId;
    @NotNull
    private final int accountRecipientId;
    @Min(value = 0, message = "Enter amount greater than zero.")
    private final double amount;

    public TransferResponseDto(int accountSenderId,
                       int actRecipientId,
                       double amount) {

        this.accountSenderId = accountSenderId;
        this.accountRecipientId = actRecipientId;
        this.amount = amount;
    }


    @NotNull
    public int getAccountSenderId() {
        return accountSenderId;
    }

    @NotNull
    public int getAccountRecipientId() {
        return accountRecipientId;
    }

    @Min(value = 0, message = "Enter amount greater than zero.")
    public double getAmount() {
        return amount;
    }
}
