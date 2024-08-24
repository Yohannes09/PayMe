package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.repository.JdbcAccountRepository;

import java.util.Optional;

public class RestAccountService implements AccountService{
    private final AccountRepository accountRepository;

    public RestAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public RestAccountService() {
        this.accountRepository = new JdbcAccountRepository();
    }


    @Override
    public Optional<Account> getAccountById(int accountId) {
        return accountRepository.getByAccountId(accountId);
    }

    @Override
    public Optional<Account> getAccountByUserId(int userId) {
        return accountRepository.getByUserId(userId);
    }
}
