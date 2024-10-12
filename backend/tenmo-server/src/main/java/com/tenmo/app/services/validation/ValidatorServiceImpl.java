package com.tenmo.app.services.validation;

import com.tenmo.app.dto.transfer.TransferRequestDto;
import com.tenmo.app.entity.Account;
import com.tenmo.app.exception.BadRequestException;
import com.tenmo.app.exception.NotFoundException;
import com.tenmo.app.exception.TransferException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ValidatorServiceImpl implements ValidatorService{

    @Override
    public void validateNewTransfer(
            TransferRequestDto request,
            Account accountFrom,
            List<Account> accountsTo
    ) throws TransferException, BadRequestException {

        // BEWARE: accountFrom added to the accountTo list.
        accountsTo.add(accountFrom);
        String currentTransferCurrency = request.currency().name();

        // Check transfer amount greater than zero.
        if(request.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new TransferException("Transfer must be greater than zero. ");

        // Check if sender has enough funds.
        // Subtract 1 since we add accountFrom to the accounts.
        BigDecimal recipientAccounts = BigDecimal.valueOf(accountsTo.size() - 1);
        BigDecimal totalTransferAmount = request.amount().multiply(recipientAccounts);
        if(accountFrom.getBalance().compareTo(totalTransferAmount) < 0)
            throw new TransferException("Sender has insufficient funds. ");

        // Ensure every account is active.
        boolean hasInactiveAccount = accountsTo
                .stream()
                .anyMatch(account -> !account.isActive());
        if(hasInactiveAccount) {
            throw new BadRequestException("1 or more accounts inactive");
        }

        // Ensure every account has matching currencies.
        boolean hasCurrencyMismatch = accountsTo
                .stream()
                .anyMatch(account ->
                        !account.getCurrency().name().equals(currentTransferCurrency));
        if(hasCurrencyMismatch) {
            throw new BadRequestException("1 or more mismatched currencies. ");
        }

    }

    public void validateExistingTransfer(UUID transferId)
            throws NotFoundException, BadRequestException{

    }


}
