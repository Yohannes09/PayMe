package com.techelevator.tenmo.service;

import com.techelevator.tenmo.entity.Account;
import com.techelevator.tenmo.repository.AccountRepository;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestAccountService implements AccountService{
    private final AccountRepository accountRepository;

    public RestAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public Optional<Account> getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public Optional<Account> getAccountByUserId(Long userId) {
        return null;
    }

    @Override
    public List<Account> getAccounts() {
        try {
            return accountRepository.findAll();
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("There was an issue getting accounts. ");
            return List.of();
        }
    }
}
