package com.techelevator.tenmo.repository.notused;

import com.techelevator.tenmo.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Optional<Account> getByAccountId(int accountId);

    Optional<Account> getByUserId(int userId);

    void withdraw(int id, double amountWithdrawn);

    void deposit(int id, double amountDeposited);

    int deleteById(int id);

    boolean accountExists(int id);

    List<Account> getAccounts();
}
