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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.util.*;

public class RestTransferService implements TransferService{
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTransferService.class);

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

    /**
     * @param transferTypeId  the type of transfer; 1 represents a request, and 2 represents a send.
     * @param transferStatusId  the status of the transfer; 1 is pending, 2 is accepted, and 3 is rejected.*/

    @Override
    public List<TransferHistoryDto> getAccountHistory(int accountId) {

        return transferRepository.getTransferHistory(accountId);
    }

    @Override
    public Optional<Transfer> getTransferById(int transferId) {
        try {
            return transferRepository.getTransferById(transferId);
        } catch (RestClientException clientException) {
            System.out.println("Error: " + clientException.getMessage());
            return Optional.empty();
        }
    }

    /*  The transfer must be accepted regardless whether it was requested or sent. Thus, tranfer status isn't included */
    @Override
    public Optional<Transfer> processTransfer(int transferTypeId, int senderAccountId, int recipientAccountId, double amount){
        try {
            validateTransfer(transferTypeId, 1, senderAccountId, recipientAccountId, amount);
            return transferRepository.proccessTransfer(transferTypeId, 1, senderAccountId, recipientAccountId, amount);
        }catch (TransferException transferException){
            LOGGER.error("TransferException: " , transferException);
        }catch (Exception e){
            LOGGER.error("Generic exception. Error: " , e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Transfer> acceptTransfer(int transferId) {
        return updateTransferStatus(transferId, 2);
    }

    @Override
    public Optional<Transfer> declineTransfer(int transferId) {
        return updateTransferStatus(transferId, 3);
    }

    @Transactional
    private Optional<Transfer> updateTransferStatus(int transferId, int newTransferStatusId){
        Optional<Transfer> transfer = transferRepository.getTransferById(transferId);

        if(transfer.isPresent()){
            Transfer updatedTransfer = transfer.get();

            if(newTransferStatusId != 2) {
                transferRepository.updateTransferStatus(updatedTransfer.getTransferId(), newTransferStatusId);
                return transferRepository.updateTransferStatus(transferId, newTransferStatusId);
            }
            else{
               accountRepository.withdraw(updatedTransfer.getSenderAccountId(),updatedTransfer.getAmount());
               accountRepository.deposit(updatedTransfer.getRecipientAccountId(), updatedTransfer.getAmount());
               return transferRepository.updateTransferStatus(updatedTransfer.getTransferId(), newTransferStatusId);
            }
        }
        return Optional.empty();
    }

    private void validateTransfer
            (int transferTypeId, int transferStatusId, int senderAccountId, int recipientAccountId, double amount) throws TransferException, AccountException{

        if(senderAccountId == recipientAccountId)
            throw new TransferException("Sender and recipient IDs cannot be the same. ");

        if(amount <= 0)
            throw new TransferException("Amount must be greater than zero. ");

        if(!accountRepository.accountExists(senderAccountId) || !accountRepository.accountExists(recipientAccountId))
            throw new AccountException("One or both accounts do not exist. ");
    }

}
