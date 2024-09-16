package com.techelevator.tenmo.services.main;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountByUserId(Long userId);

    List<Account> getAccounts();

    List<TransferResponseDto> getAccountTransferHistory(Long accountId);

    List<Account> findByAccountId(List<Long> accountIds);
}
