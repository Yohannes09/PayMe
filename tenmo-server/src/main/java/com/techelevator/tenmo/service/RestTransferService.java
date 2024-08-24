package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.repository.JdbcAccountRepository;
import com.techelevator.tenmo.repository.JdbcTransferRepository;
import com.techelevator.tenmo.repository.TransferRepository;

import java.util.*;

public class RestTransferService implements TransferService{
    private static final Map<String, Integer> TRANSFER_STATUS = getTransferStatus();
    private static final Map<String, Integer> TRANASFER_TYPE = getTransferType();

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public RestTransferService(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    public RestTransferService(){
        this.accountRepository = new JdbcAccountRepository();
        this.transferRepository = new JdbcTransferRepository();
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

    // Will be used for approving or denying transactions
    @Override
    public void processTransferRequest(boolean accepted, int senderAccountId, int recipientAccountId, int transferId) {
//        if(accepted)
//            processTransfer(senderAccountId, recipientAccountId);
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


    /*There should be a method which calls the transfer_type and transfer_status DBs to get the
    * correct IDs instead of hard coding them here.
    *
    * -Need:
    *    - Both repo classes complete.
    * */

    public static Map<String, Integer> getTransferStatus(){

        Map<String, Integer> transferStatusCodes = new HashMap<>();
        transferStatusCodes.put("pending", 1);
        transferStatusCodes.put("approved", 2);
        transferStatusCodes.put("rejected", 3);

        return transferStatusCodes;
    }

    public static Map<String, Integer> getTransferType(){
        Map<String, Integer> transferTypeCodes = new HashMap<>();
        transferTypeCodes.put("request", 1);
        transferTypeCodes.put("send", 2);

        return transferTypeCodes;
    }
}
