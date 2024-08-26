package com.techelevator.tenmo.dto;

import javax.validation.constraints.NotNull;

public class AccountDto {
    @NotNull
    private int accountId;

    @NotNull
    private int userId;

    private double balance;

    public AccountDto(int accountId, int userId, double balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public AccountDto() {

    }


    @NotNull
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(@NotNull int accountId) {
        this.accountId = accountId;
    }

    @NotNull
    public int getUserId() {
        return userId;
    }

    public void setUserId(@NotNull int userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
