package com.tenmo.app.services.validation;

import com.tenmo.app.dto.transfer.TransferRequestDto;
import com.tenmo.app.entity.Account;
import com.tenmo.app.exception.BadRequestException;
import com.tenmo.app.exception.NotFoundException;
import com.tenmo.app.exception.TransferException;
import com.tenmo.app.util.Currency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ValidatorServiceImpl implements ValidatorService{

    @Override
    public void validateNewTransfer(
            TransferRequestDto request,
            Account accountFrom,
            List<Account> accountsTo
    ) throws TransferException, BadRequestException {

        List<Account> allAccounts = mergeAllAccounts(accountFrom, accountsTo);

        if(request.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new TransferException("Transfer must be greater than zero. ");

        hasSufficientBalance(accountFrom, accountsTo, request.amount());

        hasInactiveAccount(allAccounts);

        hasCurrencyMismatch(allAccounts, request.currency());

    }

    public void validateExistingTransfer(UUID transferId)
            throws NotFoundException, BadRequestException{
//        hasSufficientBalance();
//
//        hasInactiveAccount();
    }



    private void hasSufficientBalance(
            Account accountFrom,
            List<Account> accountsTo,
            BigDecimal amount
    ){
        BigDecimal totalAmount = amount.multiply(BigDecimal.valueOf(accountsTo.size()));

        if(accountFrom.getBalance().compareTo(totalAmount) < 0) {
            throw new TransferException("Sender has insufficient funds. ");
        }

    }

    private void hasInactiveAccount(
            List<Account> accounts
    ){
        boolean hasInactiveAccount = accounts
                .stream()
                .anyMatch(account -> !account.isActive()
        );

        if(hasInactiveAccount) {
            throw new BadRequestException("1 or more accounts inactive");
        }

    }

    private void hasCurrencyMismatch(
            List<Account> accounts, Currency currency
    ) {

        boolean hasCurrencyMismatch = accounts
                .stream()
                .anyMatch(account ->
                        !account.getCurrency().equals(currency)
        );

        if(hasCurrencyMismatch) {
            throw new BadRequestException("1 or more mismatched currencies. ");
        }
    }


    private List<Account> mergeAllAccounts(Account accountFrom, List<Account> accountsTo){
        List<Account> allAccounts = accountsTo;
        accountsTo.add(accountFrom);
        return allAccounts;
    }
}
