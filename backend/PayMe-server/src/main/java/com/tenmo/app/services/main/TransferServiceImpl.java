package com.tenmo.app.services.main;

import com.tenmo.app.dto.transfer.TransferRequestDto;
import com.tenmo.app.dto.transfer.TransferResponseDto;
import com.tenmo.app.entity.Account;
import com.tenmo.app.entity.User;
import com.tenmo.app.exception.TransferException;
import com.tenmo.app.util.TransferType;
import com.tenmo.app.exception.BadRequestException;
import com.tenmo.app.exception.NotFoundException;
import com.tenmo.app.entity.Transfer;
import com.tenmo.app.mapper.TransferMapper;
import com.tenmo.app.repository.AccountRepository;
import com.tenmo.app.repository.TransferRepository;
import com.tenmo.app.services.validation.ValidatorService;
import com.tenmo.app.util.TransferStatus;
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
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j // var name to log is: log. log.warn(), log.info() , etc
@Transactional
@Service
public class TransferServiceImpl implements TransferService {
    private static final Logger log = LoggerFactory.getLogger(TransferServiceImpl.class);

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final ValidatorService validatorService;


    public TransferServiceImpl(
            AccountRepository accountRepository,
            TransferRepository transferRepository,
            ValidatorService validatorService) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.validatorService = validatorService;
    }


    @Override
    public List<TransferResponseDto> findAllByTransferId(List<UUID> transferIds){
        return transferRepository.transferResponse(transferIds);
    }


    @Override
    public TransferResponseDto handleDirectTransfer(TransferRequestDto request){
        return processTransferRequest(
                request, TransferType.SEND, TransferStatus.COMPLETED
        );
    }

    @Override
    public TransferResponseDto handleTransferRequest(TransferRequestDto request){
        return processTransferRequest(
                request, TransferType.REQUEST, TransferStatus.PENDING
        );
    }

    private TransferResponseDto processTransferRequest(
            TransferRequestDto request,
            TransferType type,
            TransferStatus status
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

        Transfer newTransfer = TransferMapper.mapRequestToTransfer(request);
        newTransfer.setType(type);
        newTransfer.setStatus(status);

        if(type == TransferType.SEND) {
            handleAccountBalances(request.amount(), accountFrom, accountsTo);
        }

        transferRepository.save(newTransfer);

        return transferRepository.transferResponse((newTransfer.getTransferId()))
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
    public TransferResponseDto handleApprovedRequest(UUID transferId){
        Transfer transfer = updatePendingTransfer(
                transferId,
                TransferStatus.COMPLETED);

        return transferRepository.transferResponse(transfer.getTransferId())
                .orElseThrow(() -> new NotFoundException("Transfer response not found with ID : " + transferId));
    }

    @Override
    public TransferResponseDto handleRejectedRequest(UUID transferId){
        updatePendingTransfer(
                transferId,
                TransferStatus.REJECTED);

        return transferRepository.transferResponse(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer response not found with ID : " + transferId));
    }

    private Transfer updatePendingTransfer(UUID transferId, TransferStatus newStatus) {
        validatorService.validateExistingTransfer(transferId);

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer with ID: " + transferId + " could not be found. "));

        transfer.setStatus(newStatus);

        return transferRepository.save(transfer);
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
