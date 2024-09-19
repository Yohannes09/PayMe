package com.tenmo.services.main;

import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountByUserId(Long userId);

    List<Account> getAccounts();

    List<TransferResponseDto> getAccountTransferHistory(Long accountId);

    List<Account> findByAccountId(List<Long> accountIds);
}
