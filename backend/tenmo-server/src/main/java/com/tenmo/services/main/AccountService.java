package com.tenmo.services.main;

import com.tenmo.dto.account.AccountDto;
import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.entity.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    List<AccountDto> getAllAccounts();

    List<TransferResponseDto> accountTransferHistory(Long accountId,
                                                   Optional<String> statusDescription,
                                                   Optional<String> typeDescription);

    List<Account> findAllByAccountId(@lombok.NonNull List<UUID> accountIds);
}
