package com.payme.app.services.validation;

import com.payme.app.dto.transaction.TransactionRequestDto;
import com.payme.app.entity.Account;
import com.payme.app.exception.BadRequestException;
import com.payme.app.exception.NotFoundException;
import com.payme.app.exception.TransactionException;
import com.payme.app.util.Currency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ValidatorServiceImpl implements ValidatorService{

    @Override
    public void validateNewTransfer(
            TransactionRequestDto request,
            Account accountFrom,
            List<Account> accountsTo
    ) throws TransactionException, BadRequestException {

        List<Account> allAccounts = mergeAllAccounts(accountFrom, accountsTo);

        if(request.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new TransactionException("Transfer must be greater than zero. ");

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
            throw new TransactionException("Sender has insufficient funds. ");
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
