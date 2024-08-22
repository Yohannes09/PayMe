package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> getByAccountId(int id);

    void withdraw(int id, double amountWithdrawn);

    void deposit(int id, double amountDeposited);

    int deleteById(int id);

    boolean accountExists(int id);
}
