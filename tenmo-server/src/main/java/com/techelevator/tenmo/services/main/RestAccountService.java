package com.techelevator.tenmo.services.main;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.entity.Account;
import com.techelevator.tenmo.exception.NotFoundException;
import com.techelevator.tenmo.repository.AccountRepository;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestAccountService implements AccountService {
    private final AccountRepository accountRepository;

    public RestAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public Optional<Account> getAccountByUserId(Long userId) {
        return null;
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<TransferResponseDto> getAccountTransferHistory(Long accountId) {
        return Optional.ofNullable(
                accountRepository.getAccountTransferHistory(accountId)
        ).orElseThrow(()-> new NotFoundException(""));
    }

}
