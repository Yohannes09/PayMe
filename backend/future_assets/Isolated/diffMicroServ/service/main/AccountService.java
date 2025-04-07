package com.payme.authentication.diffMicroServ.service.main;

import com.payme.authentication.diffMicroServ.dto.AccountDto;
import com.payme.authentication.diffMicroServ.dto.TransactionResponseDto;
import com.payme.authentication.diffMicroServ.entity.Account;
import com.payme.authentication.diffMicroServ.mapper.AccountMapper;
import com.payme.authentication.diffMicroServ.repository.AccountRepository;
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
public class AccountService{
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }



    public List<AccountDto> getAllAccounts() {
        return accountRepository
                .findAll()
                .stream()
                .map(AccountMapper::mapAccountToDto)
                .collect(Collectors.toList());
    }


    public List<Account> findAllByAccountId(@NonNull @NotEmpty List<UUID> accountIds) {
        return accountRepository.findAllById(accountIds);
    }


    public List<TransactionResponseDto> accountTransferHistory(
            UUID accountId,
            Optional<String> transferType,
            Optional<String> transferStatus){

//        List<TransactionResponseDto> transfers = accountRepository
//                .accountTransferHistory(accountId)
//                .orElseThrow(()-> new NotFoundException("Account not found. "));
//
//        return transfers.stream()
//                .filter(transfer ->transferType
//                        .map(type-> transfer.getType().name().equals(transferType))
//                        .orElse(true)
//                )
//                .filter(transfer -> transferStatus
//                        .map(status -> transfer.getStatus().name().equals(transferStatus))
//                        .orElse(true)
//                )
//                .collect(Collectors.toList());
        return null;
    }

}