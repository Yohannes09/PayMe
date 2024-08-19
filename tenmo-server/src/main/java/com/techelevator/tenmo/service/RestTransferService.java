package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestTransferService implements TransferService {
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public RestTransferService(TransferRepository transferRepository,
                               AccountRepository accountRepository,
                               UserRepository userRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public RestTransferService(){
        this.transferRepository = new JdbcTransferRepository();
        this.accountRepository = new JdbcAccountRepository();
        this.userRepository = new JdbcUserRepository();
    }

    @Override
    public Optional<Transfer> processTransfer(int senderId, int recipientId, double amount){
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

    @Override
    public Optional<User> getUserByUserId(int id){
        return Optional.ofNullable(userRepository.getUserById(id));
    }

    @Override
    public Optional<Account> getAccountByUserId(int userId) {
        return accountRepository.getAccountByUserId(userId);
    }

}
