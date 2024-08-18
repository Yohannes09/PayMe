package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.Optional;

public interface AccountDao{
    Optional<Account> findByAccountId(int id);
    Optional<Account> findByUsername(String username);
    boolean withdraw(int id, double amountWithdrawn);
    boolean deposit(int id, double amountDeposited);
    int deleteById(int id);
    double getAccountBalance(int id);
}
