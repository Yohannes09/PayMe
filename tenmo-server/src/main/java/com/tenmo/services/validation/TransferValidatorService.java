package com.tenmo.services.validation;

import com.tenmo.dto.transfer.TransferDto;
import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.entity.Account;
import com.tenmo.exception.BadRequestException;
import com.tenmo.exception.NotFoundException;
import com.tenmo.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferValidatorService implements ValidatorService{
    private final AccountRepository accountRepository;

    public TransferValidatorService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public void validateNewTransfer(TransferRequestDto request) throws NotFoundException, BadRequestException {
        validateAccounts(request.getAccountFromId(), request.getAccountToId());
        validateAccountBalance(request.getAccountFromId(), request.getAmount());
    }

//    public void validateExistingTransfer(TransferDto dto) throws NotFoundException, BadRequestException{
//        validateAccountBalance(dto.getAccountFromId(), dto.getAmount());
//    }


    private void validateAccounts(Long accountFromId, Long accountToId){
        if(!accountRepository.existsById(accountFromId) ||
                !accountRepository.existsById(accountToId)) {
            throw new NotFoundException("One or more accounts not found. ");
        }
    }

    private void validateAccountBalance(Long accountFromId, BigDecimal amount){
        Account account = accountRepository.findById(accountFromId)
                .orElseThrow(() -> new NotFoundException("Sender account Id not found. "));

        if(account.getBalance().compareTo(amount) < 0)
            throw new BadRequestException("Sender has insufficient funds. ");
    }

}
