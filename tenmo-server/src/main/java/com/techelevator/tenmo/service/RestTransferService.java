package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.repository.JdbcAccountRepository;
import com.techelevator.tenmo.repository.JdbcTransferRepository;
import com.techelevator.tenmo.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestTransferService implements TransferService {
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    public RestTransferService(TransferRepository transferRepository,
                               AccountRepository accountRepository) {

        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public RestTransferService(){
        this.transferRepository = new JdbcTransferRepository();
        this.accountRepository = new JdbcAccountRepository();
    }

    @Override
    public Optional<Transfer> processTransfer(int senderId, int recipientId, double amount){
        // sender can't send himself money
        if(senderId == recipientId)
            return Optional.empty();

        Optional<Transfer> transfer = null;
        if(accountRepository.accountExists(senderId) && accountRepository.accountExists(recipientId)){
            transfer = transferRepository.createTransfer(senderId, recipientId, amount, 2, 2, amount);
            accountRepository.withdraw(senderId, amount);
            accountRepository.deposit(recipientId, amount);
        }
        return transfer;
    }

    public void processDeposit(int accountId, double amount){
        if(amount > 0)
            accountRepository.deposit(accountId, amount);
    }

    @Override
    public double getAccountIdBalance(int id){
        return accountRepository.getAccountBalance(id);
    }

    @Override
    public Optional<Transfer> getTransferById(int id) {
        return transferRepository.getTransferById(id);
    }


}
