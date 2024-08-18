package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestTransactionService implements TransactionService{
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    public RestTransactionService(TransferRepository transferRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public Optional<Transfer> send(int senderId, int recipientId, double amount){
        if(senderId == recipientId)
            return null;

        Optional<Transfer> transfer = null;
        if(accountRepository.accountExists(senderId) && accountRepository.accountExists(recipientId)){
            transfer = transferRepository.createTransfer(senderId, recipientId, amount, 2, 2, amount);
            accountRepository.withdraw(senderId, amount);
            accountRepository.deposit(recipientId, amount);
        }
        return transfer;
    }

    public void deposit(int accountId, double amount){
        if(amount > 0)
            accountRepository.deposit(accountId, amount);
    }

    public double getBalance(int id){
        return accountRepository.getAccountBalance(id);
    }
}
