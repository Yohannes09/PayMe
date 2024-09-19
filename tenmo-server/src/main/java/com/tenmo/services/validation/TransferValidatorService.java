package com.tenmo.services.validation;

import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.entity.Account;
import com.tenmo.exception.BadRequestException;
import com.tenmo.exception.NotFoundException;
import com.tenmo.repository.AccountRepository;
import com.tenmo.util.TransferStatus;
import com.tenmo.util.TransferType;
import org.springframework.stereotype.Service;

@Service
public class TransferValidatorService implements ValidatorService{
    private final AccountRepository accountRepository;

    public TransferValidatorService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void validateTransferRequest(TransferRequestDto request) throws NotFoundException, BadRequestException {
        validateAccounts(request);
        validateTypeAndStatus(request);
        validateAccountBalance(request);
    }


    private void validateAccounts(TransferRequestDto request){
        if(accountRepository.existsById(request.getAccountFromId())
                && accountRepository.existsById(request.getAccountToId()))
            throw new NotFoundException("One or more accounts not found. ");
    }

    private void validateAccountBalance(TransferRequestDto request){
        Account account = accountRepository
                .findById(request.getAccountFromId())
                .orElseThrow(() -> new NotFoundException("Sender account Id not found. "));

        if(account.getBalance().compareTo(request.getAmount()) >= 0)
            throw new BadRequestException("Type or status ID is incorrect. ");
    }

    private void validateTypeAndStatus(TransferRequestDto request){
        if(TransferType.getValidTransferTypes().contains(request.getTransferTypeId()) &&
                TransferStatus.getValidTransferStatus().contains(request.getTransferStatusId()))
            throw new BadRequestException("Sender has insufficient funds .");
    }

}
