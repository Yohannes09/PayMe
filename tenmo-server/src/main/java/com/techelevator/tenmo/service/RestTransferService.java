package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.exception.AccountException;
import com.techelevator.tenmo.exception.TransferException;
import com.techelevator.tenmo.entity.Transfer;
import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.*;

@Service
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


    /*
     * transferTypeId:  1 a request, and 2 a send.
     * transferStatusId:    1 is pending, 2 is accepted, and 3 is rejected.*/

    @Override
    public List<TransferResponseDto> getAccountHistory(Long accountId) {
        return transferRepository.getTransferHistory(accountId);
    }

    @Override
    public Optional<Transfer> getTransferById(Long transferId) {
        try {
            return Optional.ofNullable(transferRepository.findTransferById(transferId));
        }catch (RestClientException clientException) {
            System.out.println("Error: " + clientException.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<TransferResponseDto> accountTransferStatus(Integer transferStatusId, Long accountId) {
        return accountRepository.existsById(accountId)?
                transferRepository.accountTransferStatus(transferStatusId, accountId):List.of();
    }

    @Transactional
    @Override
    public Optional<Transfer> processTransfer(
            Integer transferTypeId, Long accountFromId, Long accountToId, BigDecimal amount, String transferMessage){

        try {
            validateTransfer(null, transferTypeId, accountFromId, accountToId, amount);

            if(transferTypeId == 1) {
                return handleTransferRequest(accountFromId, accountToId, amount, transferMessage);
            }
            else if (transferTypeId == 2) {
                return handleDirectTransfer(accountFromId, accountToId, amount, transferMessage);
            }

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
    public Optional<Transfer> updatePendingTransfer(Long transferId, Integer newTransferStatusId) {
        Integer requestId = 1;
        Optional<Transfer> transferOpt = Optional.ofNullable(transferRepository.findTransferById(transferId));
        try {
            validateTransfer(transferId, requestId, transferOpt.get().getAccountFrom(), transferOpt.get().getAccountTo(), transferOpt.get().getTransferAmount());
            Transfer transfer = transferOpt.get();

            if (newTransferStatusId == 2) {
                handleAccountBalances(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getTransferAmount());
            }
            return Optional.ofNullable(transferRepository.updateTransferStatus(transferId, newTransferStatusId));
        } catch (TransferException e) {
            throw new RuntimeException(e);
        }catch (AccountException accountException){
            throw new RuntimeException(accountException);
        }
    }

    //************* Helper methods***********
    private Optional<Transfer> handleTransferRequest(Long accountFromId, Long accountToId, BigDecimal amount, String transferMessage){
        return Optional.ofNullable(transferRepository.processTransfer(1, 1, accountFromId, accountToId, amount, transferMessage));
    }

    private Optional<Transfer> handleDirectTransfer(Long accountFromId, Long accountToId, BigDecimal amount, String transferMessage){
        handleAccountBalances(accountFromId, accountToId, amount);
        return Optional.ofNullable(transferRepository.processTransfer(2, 2, accountFromId, accountToId, amount, transferMessage));
    }

    private void handleAccountBalances(Long accountFromId, Long accountToId, BigDecimal amount){
        accountRepository.updateBalance(accountFromId, amount.negate());
        accountRepository.updateBalance(accountToId, amount);
    }

    private void validateTransfer
            (Long transferId, Integer transferTypeId, Long accountFromId, Long accountToId, BigDecimal amount) throws TransferException, AccountException{

        if(accountFromId.equals(accountToId))
            throw new TransferException("Sender and recipient IDs cannot be the same. ");

        if(amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new TransferException("Amount must be greater than zero. ");

        if(!VALID_TYPE_IDS.contains(transferTypeId))
            throw new TransferException("Invalid transfer type ID. ");

        if(!accountRepository.existsById(accountFromId) || !accountRepository.existsById(accountToId))
            throw new AccountException("One or both accounts do not exist. ");

        if(transferId != null){
            Optional<Transfer> transferOptional = Optional.ofNullable(transferRepository.findTransferById(transferId));
            if(transferOptional.isEmpty())
                throw new TransferException("Transfer does not exist. ");

            Transfer transfer = transferOptional.get();

            // If a transfer was Sent
            if ((transfer.getTransferTypeId() == 2 || transfer.getTransferStatusId() == 2) ||
                    (transfer.getTransferTypeId() == 1 && transfer.getTransferStatusId() == 3)) {
                throw new TransferException("Transfer already processed");
            }

        }
    }

}
