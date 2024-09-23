package com.tenmo.services.main;

import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountByUserId(Long userId);

    List<Account> getAccounts();

    List<TransferResponseDto> accountTransferHistory(Long accountId);

    List<TransferResponseDto> accountTransferHistory(Long accountId,
                                                   Optional<String> statusDescription,
                                                   Optional<String> typeDescription);

    Optional<List<Account>> findByAccountId(List<Long> accountIds);
}
