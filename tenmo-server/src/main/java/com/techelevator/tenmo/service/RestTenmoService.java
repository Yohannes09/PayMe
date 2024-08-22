package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repository.*;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

@Service
public class RestTenmoService implements TenmoService {
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public RestTenmoService(TransferRepository transferRepository,
                            AccountRepository accountRepository,
                            UserRepository userRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public RestTenmoService(){
        this.transferRepository = new JdbcTransferRepository();
        this.accountRepository = new JdbcAccountRepository();
        this.userRepository = new JdbcUserRepository();
    }


    @Override
    public Optional<Transfer> processTransfer(int senderAccountId, int recipientId, double amount){
        if(senderAccountId == recipientId)
            return Optional.empty();

        Optional<Transfer> transfer = null;
        if(accountRepository.accountExists(senderAccountId) &&
                accountRepository.accountExists(recipientId)){

            transfer = transferRepository.proccessTransfer(senderAccountId, recipientId, 2, 2, amount);
            accountRepository.withdraw(senderAccountId, amount);
            accountRepository.deposit(recipientId, amount);
        }
        return transfer;
    }

    @Override
    public Optional<Transfer> requestTransfer(int senderAccountId, int recipientAccountId, double amount) {
        if(senderAccountId == recipientAccountId)
            return Optional.empty();

        if(accountRepository.accountExists(senderAccountId) &&
                accountRepository.accountExists(recipientAccountId)){

            return transferRepository.proccessTransfer(
                    senderAccountId,
                    recipientAccountId,
                    1,
                    1,
                    amount
            );
        }
        return Optional.empty();
    }

    @Override
    public void processTransferRequest(boolean accepted, int senderAccountId, int recipientAccountId, int transferId) {
//        if(accepted)
//            processTransfer(senderAccountId, recipientAccountId)
    }

    @Override
    public void processDeposit(int accountId, double balance){
        try {
            if(balance > 0)
                accountRepository.deposit(accountId, balance);
        } catch (InputMismatchException e) {

        }
    }

    @Override
    public void processWithdraw(int accountId, double balance) {
        accountRepository.withdraw(accountId, balance);
    }

    @Override
    public List<Transfer> accountTransferHistory(int accountId) {
        return transferRepository.accountTransferHistory(accountId);
    }

    @Override
    public Optional<Transfer> getTransferById(int transferId) {
        return transferRepository.getTransferById(transferId);
    }

    @Override
    public List<Transfer> accountPendingTransfers(int accountId) {
        return transferRepository.getPendingRequests(accountId);
    }

    @Override
    public Optional<Account> getAccountById(int accountId) {
        return accountRepository.getByAccountId(accountId);
    }

    @Override
    public Optional<User> getUserById(int userId) {
        return Optional.ofNullable(userRepository.getUserById(userId));
    }

}
