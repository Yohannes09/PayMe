package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dto.TransferHistoryDto;
import com.techelevator.tenmo.exception.AccountException;
import com.techelevator.tenmo.exception.TransferException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.repository.JdbcAccountRepository;
import com.techelevator.tenmo.repository.JdbcTransferRepository;
import com.techelevator.tenmo.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.util.*;

//@Component
public class RestTransferService implements TransferService{
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTransferService.class);
    private static final Set<Integer> VALID_TYPE_IDS = Set.of(1,2);
    private static final Set<Integer> VALID_STATUS_IDS = Set.of(1,2,3);

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


    /*
     * transferTypeId:  1 a request, and 2 a send.
     * transferStatusId:    1 is pending, 2 is accepted, and 3 is rejected.*/

    @Override
    public List<TransferHistoryDto> getAccountHistory(int accountId) {

        return transferRepository.getTransferHistory(accountId);
    }

    @Override
    public Optional<Transfer> getTransferById(int transferId) {
        try {
            return transferRepository.getTransferById(transferId);
        }catch (RestClientException clientException) {
            System.out.println("Error: " + clientException.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<TransferHistoryDto> accountTransferStatus(int transferStatusId, int accountId) {
        return accountRepository.accountExists(accountId)?
                transferRepository.accountTransferStatus(transferStatusId, accountId):List.of();
    }

    @Transactional
    @Override
    public Optional<Transfer> processTransfer(int transferTypeId, int senderAccountId, int recipientAccountId, double amount){

        try {
            validateTransfer(transferTypeId, senderAccountId, recipientAccountId, amount);

            // Handle transfer requests.
            if(transferTypeId == 1)
                return transferRepository.processTransfer(1, 1, senderAccountId, recipientAccountId, amount);

            // Handle direct transfer.
            accountRepository.withdraw(senderAccountId, amount);
            accountRepository.deposit(recipientAccountId, amount);

            return transferRepository.processTransfer(2, 2, senderAccountId, recipientAccountId, amount);
        }catch (TransferException transferException){
            LOGGER.error("TransferException: " , transferException);
            System.out.println(transferException.getMessage());
        }catch (Exception e){
            LOGGER.error("Generic exception. Error: " , e.getMessage());
            System.out.println("" + e.getMessage());
        }

        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Transfer> updatePendingTransfer(int transferId, int newTransferStatusId) {
        Optional<Transfer> transferOpt = transferRepository.getTransferById(transferId);

        try {
            if(!VALID_STATUS_IDS.contains(newTransferStatusId))
                throw new TransferException("Invalid transfer status ");
            if (transferOpt.isEmpty())
                throw new TransferException("Transfer not found.");
            Transfer transfer = transferOpt.get();
            if (transfer.getTransferStatusId() == 2 || transfer.getTransferStatusId() == 3)
                throw new TransferException("Transfer already processed.");


            if (newTransferStatusId == 2) {
                accountRepository.withdraw(transfer.getSenderAccountId(), transfer.getAmount());
                accountRepository.deposit(transfer.getRecipientAccountId(), transfer.getAmount());
            }
            return transferRepository.updateTransferStatus(transferId, newTransferStatusId);
        } catch (TransferException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateTransfer
            (int transferTypeId, int senderAccountId, int recipientAccountId, double amount) throws TransferException, AccountException{

        if(senderAccountId == recipientAccountId)
            throw new TransferException("Sender and recipient IDs cannot be the same. ");

        if(amount <= 0)
            throw new TransferException("Amount must be greater than zero. ");

        if(VALID_TYPE_IDS.contains(transferTypeId))
            throw new TransferException("Invalid transfer type ID. ");

        if(!accountRepository.accountExists(senderAccountId) || !accountRepository.accountExists(recipientAccountId))
            throw new AccountException("One or both accounts do not exist. ");
    }

}
