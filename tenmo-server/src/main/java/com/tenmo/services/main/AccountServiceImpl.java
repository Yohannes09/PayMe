package com.tenmo.services.main;

import com.tenmo.dto.account.AccountDto;
import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.entity.Account;
import com.tenmo.mapper.AccountMapper;
import com.tenmo.repository.AccountRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public List<AccountDto> getAllAccounts() {
        return accountRepository
                .findAll()
                .stream()
                .map(AccountMapper::mapAccountToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<Account> findAllByAccountId(@NonNull List<UUID> accountIds) {
        if(accountIds.isEmpty())
            return List.of();

        return accountRepository.findAllById(accountIds);
    }

    @Override
    public List<TransferResponseDto> accountTransferHistory(
            @NonNull Long accountId,
            Optional<String> statusDescription,
            Optional<String> typeDescription){

        List<TransferResponseDto> transfers = accountRepository.accountTransferHistory(accountId).orElseThrow();

        return transfers.stream()
                .filter(transfer -> typeDescription
                        .map(type -> transfer.getTypeDescription().equals(typeDescription))
                        .orElse(true)
                ).filter(transfer ->statusDescription
                        .map(status-> transfer.getStatusDescription().equals(statusDescription))
                        .orElse(true)
                ).filter(transfer -> statusDescription
                        .map(id -> transfer.getStatusDescription().equals(statusDescription))
                        .orElse(true)
                ).collect(Collectors.toList());
    }

}
