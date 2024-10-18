package com.payme.app.services.main;

import com.payme.app.dto.transaction.TransactionRequestDto;
import com.payme.app.dto.transaction.TransactionResponseDto;
import com.payme.app.entity.Account;
import com.payme.app.util.TransactionType;
import com.payme.app.exception.BadRequestException;
import com.payme.app.exception.NotFoundException;
import com.payme.app.entity.Transaction;
import com.payme.app.mapper.TransactionMapper;
import com.payme.app.repository.AccountRepository;
import com.payme.app.repository.TransactionRepository;
import com.payme.app.services.validation.ValidatorService;
import com.payme.app.util.TransactionStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j // var name to log is: log. log.warn(), log.info() , etc
@Transactional
@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ValidatorService validatorService;


    public TransactionServiceImpl(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            ValidatorService validatorService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.validatorService = validatorService;
    }


    @Override
    public List<TransactionResponseDto> findAllByTransferId(List<UUID> transferIds){
        return transactionRepository.transferResponse(transferIds);
    }


    @Override
    public TransactionResponseDto handleDirectTransfer(TransactionRequestDto request){
        return processTransferRequest(
                request, TransactionType.SEND, TransactionStatus.COMPLETED
        );
    }

    @Override
    public TransactionResponseDto handleTransferRequest(TransactionRequestDto request){
        return processTransferRequest(
                request, TransactionType.REQUEST, TransactionStatus.PENDING
        );
    }

    private TransactionResponseDto processTransferRequest(
            TransactionRequestDto request,
            TransactionType type,
            TransactionStatus status
    ){
        Account accountFrom = accountRepository
                .findById(request.accountFromId())
                .orElseThrow(()-> new NotFoundException("Sender account not found"));

        List<Account> accountsTo = fetchAccounts(request.accountToIds());

        try {
            validatorService.validateNewTransfer(request, accountFrom, accountsTo);
        } catch (Exception e) {
            log.info("Validation error: {} ", e.getMessage());
            throw new RuntimeException(e);
        }

        Transaction newTransaction = TransactionMapper.mapRequestToTransfer(request);
        newTransaction.setType(type);
        newTransaction.setStatus(status);

        if(type == TransactionType.SEND) {
            handleAccountBalances(request.amount(), accountFrom, accountsTo);
        }

        transactionRepository.save(newTransaction);

        return transactionRepository.transferResponse((newTransaction.getTransferId()))
                .orElseThrow(()->
                        new BadRequestException("An issue occured with your transfer response. "));

    }

    private List<Account> handleAccountBalances(BigDecimal transferAmount,
                                                Account accountFrom,
                                                @NotNull @NotEmpty List<Account> accountsTo
    ){
        BigDecimal totalAmount = transferAmount
                .multiply(BigDecimal.valueOf(accountsTo.size()));

        accountFrom.setBalance(accountFrom.getBalance().subtract(totalAmount));

        accountsTo.forEach(
                account -> account.setBalance(account.getBalance().add(transferAmount))
        );

        return accountRepository.saveAll(merge(accountFrom, accountsTo));
    }



    @Override
    public TransactionResponseDto handleApprovedRequest(UUID transferId){
        Transaction transaction = updatePendingTransfer(
                transferId,
                TransactionStatus.COMPLETED);

        return transactionRepository.transferResponse(transaction.getTransferId())
                .orElseThrow(() -> new NotFoundException("Transfer response not found with ID : " + transferId));
    }

    @Override
    public TransactionResponseDto handleRejectedRequest(UUID transferId){
        updatePendingTransfer(
                transferId,
                TransactionStatus.REJECTED);

        return transactionRepository.transferResponse(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer response not found with ID : " + transferId));
    }

    private Transaction updatePendingTransfer(UUID transferId, TransactionStatus newStatus) {
        validatorService.validateExistingTransfer(transferId);

        Transaction transaction = transactionRepository.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer with ID: " + transferId + " could not be found. "));

        transaction.setStatus(newStatus);

        return transactionRepository.save(transaction);
    }

    private List<Account> fetchAccounts(List<UUID> accountIds){
        List<Account> accounts = accountRepository
                .findAllById(accountIds);

        if(accounts.size() != accountIds.size()) {
            throw new NotFoundException("One or more transfer recipient accounts not found. ");
        }

        return accounts;
    }

//    private Transfer handleRejectedTransfer(TransferRequestDto request){
//        Transfer rejectedTranfer = TransferMapper.mapRequestToTransfer(request);
//        rejectedTranfer.setType();
//    }


    private <T> List<T> merge(T t, List<T> tList){
        List<T> mergedList = tList;
        tList.add(t);
        return mergedList;
    }

}
