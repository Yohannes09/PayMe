package com.tenmo.services.validation;

import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.entity.Account;
import com.tenmo.exception.BadRequestException;
import com.tenmo.exception.NotFoundException;
import com.tenmo.repository.AccountRepository;
import com.tenmo.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ValidatorServiceImpl implements ValidatorService{
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public ValidatorServiceImpl(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    /** NEEDS<br><br>
     * <li>Validate account currency matches transfer currency. </li>
     * <li>Check if accounts are active. </li>*/

    @Override
    public void validateNewTransfer(TransferRequestDto request) throws NotFoundException, BadRequestException {
//        validateAccounts(request.getAccountFromId(), request.getAccountToId());
//        validateAccountBalance(request.getAccountFromId(), request.getAmount());
    }

    public void validateExistingTransfer(UUID transferId) throws NotFoundException, BadRequestException{
//        Transfer transfer = transferRepository.findById(transferId)
//                .orElseThrow(() -> new NotFoundException("Transfer not found with ID: " + transferId));
//
//        Account sender = accountRepository.findById(transfer.getAccountFrom())
//                .orElseThrow(() -> new NotFoundException("Sender not found ."));
//
//        if(sender.getBalance().compareTo(transfer.getAmount()) < 0)
//            throw new BadRequestException("Sender has insufficient funds");
    }



    private void validateAccounts(UUID accountFromId, UUID accountToId){
        if(!accountRepository.existsById(accountFromId) ||
                !accountRepository.existsById(accountToId)) {
            throw new NotFoundException("One or more accounts not found. ");
        }
    }

    private void validateAccountBalance(UUID accountFromId, BigDecimal amount){
        Account account = accountRepository.findById(accountFromId)
                .orElseThrow(() -> new NotFoundException("Sender account Id not found. "));

        if(account.getBalance().compareTo(amount) < 0)
            throw new BadRequestException("Sender has insufficient funds. ");
    }

}
