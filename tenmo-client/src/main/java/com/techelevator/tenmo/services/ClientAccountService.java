package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;

import java.util.Optional;

public interface ClientAccountService {
    Optional<Account> getAccountById(int accountId);

    Optional<Account> getAccountByUserId(int userId);

}
