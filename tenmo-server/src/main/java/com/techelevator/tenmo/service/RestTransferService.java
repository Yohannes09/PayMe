package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.exception.AccountException;
import com.techelevator.tenmo.exception.TransferException;
import com.techelevator.tenmo.entity.Transfer;
import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.*;

@Service
public class RestTransferService implements TransferService{
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTransferService.class);

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public RestTransferService(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }


    @Override
    public List<TransferResponseDto> getAccountHistory(Long accountId) {
        return transferRepository.getTransferHistory(accountId);
    }

    @Override
    public Optional<TransferResponseDto> getDetailedTransfer(Long transferId) {
        return Optional.ofNullable(transferRepository.getDetailedTransfer(transferId));
    }

    @Override
    public Optional<Transfer> getTransfer(Long transferId) {
        return Optional.ofNullable(transferRepository.findTransferById(transferId));
    }

    @Override
    public List<TransferResponseDto> accountTransferStatus(Integer transferStatusId, Long accountId) {
        return accountRepository.existsById(accountId)?
                transferRepository.accountTransferStatus(transferStatusId, accountId):List.of();
    }

    @Transactional
    @Override
    public Optional<Transfer> processTransfer(
            Integer transferTypeId,
            Long accountFromId,
            Long accountToId,
            BigDecimal amount,
            String transferMessage){

        try {
            validateTransfer(
                    null,
                    transferTypeId,
                    accountFromId,
                    accountToId,
                    amount
            );

            if(transferTypeId == TransferType.REQUEST.getTransferTypeId()) {
                return handleTransferRequest(accountFromId, accountToId, amount, transferMessage);
            }
            else if (transferTypeId == TransferType.SEND.getTransferTypeId()) {
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
        Optional<Transfer> transferOpt = Optional.ofNullable(transferRepository.findTransferById(transferId));

        try {
            validateTransfer(
                    transferId,
                    TransferType.REQUEST.getTransferTypeId(),
                    transferOpt.get().getAccountFrom(),
                    transferOpt.get().getAccountTo(),
                    transferOpt.get().getAmount()
            );

            Transfer transfer = transferOpt.get();

            if (newTransferStatusId == TransferStatus.APPROVED.getStatusId()) {
                handleAccountBalances(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
            }
            return Optional.ofNullable(transferRepository.updateTransferStatus(transferId, newTransferStatusId));

        } catch (TransferException e) {
            throw new RuntimeException(e);
        }catch (AccountException accountException){
            throw new RuntimeException(accountException);
        }
    }

    private Optional<Transfer> handleTransferRequest(
            Long accountFromId, Long accountToId, BigDecimal amount, String transferMessage){

        return Optional.ofNullable(
                transferRepository.processTransfer(
                        TransferType.REQUEST.getTransferTypeId(),
                        TransferStatus.PENDING.getStatusId(),
                        accountFromId,
                        accountToId,
                        amount,
                        transferMessage)
        );
    }

    private Optional<Transfer> handleDirectTransfer(
            Long accountFromId, Long accountToId, BigDecimal amount, String transferMessage){

        handleAccountBalances(accountFromId, accountToId, amount);
        return Optional.ofNullable(
                transferRepository.processTransfer(
                        TransferType.SEND.getTransferTypeId(),
                        TransferStatus.APPROVED.getStatusId(),
                        accountFromId,
                        accountToId,
                        amount,
                        transferMessage)
        );
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

        if(!TransferType.validateTypeId(transferTypeId))
            throw new TransferException("Invalid transfer type ID. ");

        if(!accountRepository.existsById(accountFromId) || !accountRepository.existsById(accountToId))
            throw new AccountException("One or both accounts do not exist. ");

        if(transferId != null){
            if(!transferRepository.existsById(transferId))
                throw new TransferException("Transfer does not exist. ");

            Transfer transfer = transferRepository.findTransferById(transferId);
            Integer currTypeId = transfer.getTransferTypeId();
            Integer currStatusId = transfer.getTransferStatusId();

            if (currTypeId == TransferType.REQUEST.getTransferTypeId() && currStatusId != TransferStatus.PENDING.getStatusId()) {
                throw new TransferException("Transfer already processed");
            }

        }

    }

}
