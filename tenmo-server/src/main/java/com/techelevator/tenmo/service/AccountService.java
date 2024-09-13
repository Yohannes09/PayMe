package com.techelevator.tenmo.service;

import com.techelevator.tenmo.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountById(Long accountId);

    Optional<Account> getAccountByUserId(Long userId);

    List<Account> getAccounts();
}
