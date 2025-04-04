package com.payme.authentication.diffMicroServ.service.main;

import com.payme.authentication.diffMicroServ.dto.TransactionRequestDto;
import com.payme.authentication.diffMicroServ.dto.TransactionResponseDto;
import com.payme.authentication.diffMicroServ.entity.Account;
import com.payme.authentication.diffMicroServ.constants.TransactionType;
import com.payme.authentication.exception.BadRequestException;
import com.payme.authentication.exception.NotFoundException;
import com.payme.authentication.diffMicroServ.entity.Transaction;
import com.payme.authentication.diffMicroServ.mapper.TransactionMapper;
import com.payme.authentication.diffMicroServ.repository.AccountRepository;
import com.payme.authentication.diffMicroServ.repository.TransactionRepository;
import com.payme.authentication.repository.UserRepository;
import com.payme.authentication.diffMicroServ.service.validation.ValidatorService;
import com.payme.authentication.diffMicroServ.constants.TransactionStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//@Slf4j // var name to log is: log. log.warn(), log.info() , etc
@Transactional
@Service
public class TransactionService{
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ValidatorService validatorService;
    private final UserRepository userRepository;


    public TransactionService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            ValidatorService validatorService,
            UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.validatorService = validatorService;
        this.userRepository = userRepository;
    }
//- **`map`** produces a `Stream<List<Transaction>>` (a stream of lists) because
// each account has a list of transactions. This results in a nested structure that requires
// additional steps to flatten.

// - **`flatMap`** flattens those lists as it processes them, producing a `Stream<Transaction>`
// directly (a single stream of transactions), which is what you need.
//
// In your case, `flatMap` avoids the extra step of flattening and directly combines all transactions from all accounts into a single flattened list.


    public List<TransactionResponseDto> getAllUserTransactions(UUID userId){
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("User not found"));

        List<Account> userAccounts = user.getAccounts();

        List<Transaction> transactions = userAccounts.stream()
                .flatMap(account -> account.getTransactions().stream())
                .collect(Collectors.toList());

        return transactions.stream()
                .map(TransactionMapper::mapTransactionToResponse)
                .collect(Collectors.toList());
    }



    public List<TransactionResponseDto> findAllByTransferId(List<UUID> transferIds){
        return transactionRepository.transferResponse(transferIds);
    }



    public TransactionResponseDto handleDirectTransfer(TransactionRequestDto request){
        return processTransferRequest(
                request, TransactionType.SEND, TransactionStatus.COMPLETED
        );
    }


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
                .findById(request.getAccountFromId())
                .orElseThrow(()-> new NotFoundException("Sender account not found"));

        List<Account> accountsTo = fetchAccounts(request.getAccountToIds());

        try {
            validatorService.validateNewTransfer(request, accountFrom, accountsTo);
        } catch (Exception e) {
            log.info("Validation error: {} ", e.getMessage());
            throw new RuntimeException(e);
        }

        Transaction newTransaction = TransactionMapper.mapRequestToTransaction(request);
        newTransaction.setType(type);
        newTransaction.setStatus(status);

        if(type == TransactionType.SEND) {
            handleAccountBalances(request.getAmount(), accountFrom, accountsTo);
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




    public TransactionResponseDto handleApprovedRequest(UUID transferId){
        Transaction transaction = updatePendingTransfer(
                transferId,
                TransactionStatus.COMPLETED);

        return transactionRepository.transferResponse(transaction.getTransferId())
                .orElseThrow(() -> new NotFoundException("Transfer response not found with ID : " + transferId));
    }


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
