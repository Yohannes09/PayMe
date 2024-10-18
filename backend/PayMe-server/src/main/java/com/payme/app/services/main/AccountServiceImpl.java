package com.payme.app.services.main;

import com.payme.app.dto.account.AccountDto;
import com.payme.app.dto.transaction.TransactionResponseDto;
import com.payme.app.entity.Account;
import com.payme.app.exception.NotFoundException;
import com.payme.app.mapper.AccountMapper;
import com.payme.app.repository.AccountRepository;
import jakarta.validation.constraints.NotEmpty;
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
    public List<Account> findAllByAccountId(@NonNull @NotEmpty List<UUID> accountIds) {
        return accountRepository.findAllById(accountIds);
    }

    @Override
    public List<TransactionResponseDto> accountTransferHistory(
            UUID accountId,
            Optional<String> transferType,
            Optional<String> transferStatus){

        List<TransactionResponseDto> transfers = accountRepository
                .accountTransferHistory(accountId)
                .orElseThrow(()-> new NotFoundException("Account not found. "));

        return transfers.stream()
                .filter(transfer ->transferType
                        .map(type-> transfer.type().name().equals(transferType))
                        .orElse(true)
                )
                .filter(transfer -> transferStatus
                        .map(status -> transfer.status().name().equals(transferStatus))
                        .orElse(true)
                )
                .collect(Collectors.toList());
    }

}