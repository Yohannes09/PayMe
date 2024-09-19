package com.tenmo.services.main;

import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.entity.Account;
import com.tenmo.entity.User;
import com.tenmo.exception.NotFoundException;
import com.tenmo.repository.AccountRepository;
import com.tenmo.repository.TransferStatusRepository;
import com.tenmo.repository.TransferTypeRepository;
import com.tenmo.util.TransferType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestAccountService implements AccountService {
    private final AccountRepository accountRepository;
    private final TransferTypeRepository typeRepository;
    private final TransferStatusRepository statusRepository;

    public RestAccountService(AccountRepository accountRepository,
                              TransferTypeRepository typeRepository,
                              TransferStatusRepository statusRepository) {
        this.accountRepository = accountRepository;
        this.typeRepository = typeRepository;
        this.statusRepository = statusRepository;
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
                                                          Optional<String> statusDescription,
                                                          Optional<String> typeDescription){

        User user = accountRepository.findUserByAccountId(accountId)
                .orElseThrow(() -> new NotFoundException("A user could not be found with the given account ID: " + accountId));

        Optional<Integer> statusId = statusDescription.map(this::mapStatusDescriptionToId);
        Optional<Integer> typeId = typeDescription.map(this::mapTypeDescriptionToId);

        List<TransferResponseDto> transfers = accountRepository.accountTransferHistory(accountId);

        return transfers.stream()
                .filter(transfer -> typeId
                        .map(id -> transfer.getTypeId().equals(id))
                        .orElse(true)
                ).filter(transfer ->
                        transfer.getAccountFromUsername().equals(user.getUsername())
                ).filter(transfer -> statusId
                        .map(id -> transfer.getStatusId().equals(id))
                        .orElse(true)
                ).collect(Collectors.toList());
    }

    private Integer mapStatusDescriptionToId(String statusDescription) {
        return statusRepository.findByStatusDescription(statusDescription)
                .orElseThrow(() -> new NotFoundException("Status not found for description: " + statusDescription))
                .getTransferStatusId();
    }

    private Integer mapTypeDescriptionToId(String typeDescription) {
        return typeRepository.findByDescription(typeDescription)
                .orElseThrow(() -> new NotFoundException("Transfer type not found for description: " + typeDescription))
                .getTypeId();
    }

}
