package com.payme.app.services.main;

import com.payme.app.dto.account.AccountDto;
import com.payme.app.dto.transaction.TransactionResponseDto;
import com.payme.app.entity.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    List<AccountDto> getAllAccounts();

    List<TransactionResponseDto> accountTransferHistory(UUID accountId,
                                                        Optional<String> statusDescription,
                                                        Optional<String> typeDescription);

    List<Account> findAllByAccountId(@lombok.NonNull List<UUID> accountIds);
}
