package com.techelevator.tenmo.model;

public class Account {
    private int userId;
    private int accountId;
    private double balance;

    public Account(){}

    public Account(int userId, int accountId, double balance){
        this.userId = userId;
        this.accountId = accountId;
        this.balance = balance;
    }


    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }


    public int getAccountId(){
        return this.accountId;
    }
    public void setAccountId(int id){
        this.accountId = id;
    }


    public int getUserId() {
        return userId;
    }
    public void setUserId(int id){
        this.userId = id;
    }
}
