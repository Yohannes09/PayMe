package com.techelevator.tenmo.services.main;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.entity.Account;
import com.techelevator.tenmo.entity.User;
import com.techelevator.tenmo.exception.NotFoundException;
import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.services.utils.TransferType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                accountRepository.accountTransferHistory(accountId)
        ).orElseThrow(()-> new NotFoundException(""));
    }

    @Override
    public List<Account> findByAccountId(List<Long> accountIds) {
        if(accountIds.isEmpty() || accountIds == null)
            return List.of();
        return accountRepository.findAllById(accountIds);
    }

    public List<TransferResponseDto> accountSentTransfers(Long accountId,
                                                          Optional<String> statusDescription){

        User user = accountRepository.findUserByAccountId(accountId)
                .orElseThrow();
        if(statusDescription.isPresent())
            return accountRepository.accountTransferHistory(accountId)
                    .stream()
                    .filter(transfer ->{
                        return transfer.getTransferStatusDescription()
                                .equals(statusDescription)
                                && transfer.getTransferTypeDescription()
                                .equals(TransferType.SEND.getTransferTypeDescription());
                    }).collect(Collectors.toList());

        return accountRepository.accountTransferHistory(accountId);
    }

}
