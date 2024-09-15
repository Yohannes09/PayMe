package com.techelevator.tenmo.services.validation;

import com.techelevator.tenmo.dto.TransferRequestDto;
import com.techelevator.tenmo.entity.Account;
import com.techelevator.tenmo.exception.BadRequestException;
import com.techelevator.tenmo.exception.NotFoundException;
import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.services.utils.TransferStatus;
import com.techelevator.tenmo.services.utils.TransferType;
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
