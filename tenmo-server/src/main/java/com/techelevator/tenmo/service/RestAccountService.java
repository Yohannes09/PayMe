package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.repository.notused.AccountRepository;
import com.techelevator.tenmo.repository.notused.JdbcAccountRepository;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import java.util.List;
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

    @Override
    public List<Account> getAccounts() {
        try {
            return accountRepository.getAccounts();
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("There was an issue getting accounts. ");
            return List.of();
        }
    }
}
