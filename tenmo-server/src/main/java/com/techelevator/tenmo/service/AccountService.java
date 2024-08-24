package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Account;

import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountById(int accountId);

    Optional<Account> getAccountByUserId(int userId);
}
