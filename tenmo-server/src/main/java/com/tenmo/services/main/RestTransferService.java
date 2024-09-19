package com.tenmo.services.main;

import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.exception.BadRequestException;
import com.tenmo.exception.NotFoundException;
import com.tenmo.entity.Transfer;
import com.tenmo.mapper.TransferMapper;
import com.tenmo.repository.AccountRepository;
import com.tenmo.repository.TransferRepository;
import com.tenmo.util.TransferStatus;
import com.tenmo.util.TransferType;
import com.tenmo.services.validation.ValidatorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class RestTransferService implements TransferService {
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final ValidatorService validatorService;

    public RestTransferService(
            AccountRepository accountRepository,
            TransferRepository transferRepository,
            ValidatorService validatorService) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.validatorService = validatorService;
    }


    @Override
    public Transfer processTransferRequest(TransferRequestDto transferRequest){
        validatorService.validateTransferRequest(transferRequest);

        if(transferRequest.getTransferTypeId()
                .equals(TransferType.SEND.getTypeId()))
            handleAccountBalances(transferRequest);

        return Optional.ofNullable(
                transferRepository.save(TransferMapper.mapDtoToTransfer(transferRequest))
        ).orElseThrow(() -> new BadRequestException(""));
    }

    @Scheduled(fixedRate = 5000) // executes every 5s
    @Override
    public List<Transfer> completeAcceptedTransfers(){
        List<Transfer> approvedTransfers =  transferRepository
                .findAll()
                .stream()
                .filter(transfer -> {
                     return transfer.getStatusId().equals(TransferStatus.PENDING.getStatusId()) &&
                             transfer.getTypeId().equals(TransferType.REQUEST.getTypeId());
                }).collect(Collectors.toList());

        approvedTransfers.forEach(transfer -> {
            handleAccountBalances(TransferMapper.mapTransferToDto(transfer));
            updatePendingTransfer(transfer.getTransferId(), TransferStatus.COMPLETED.getStatusId());
        });

        return approvedTransfers;
    }

    @Override
    public Transfer findTransferById() {
        return null;
    }


    private Optional<Transfer> updatePendingTransfer(Long transferId, Integer newTransferStatusId) {
        return Optional.ofNullable(transferRepository.updateTransferStatus(transferId, newTransferStatusId)
        ).orElseThrow(() -> new NotFoundException(""));
    }

    private void handleAccountBalances(TransferRequestDto requestDto){
        accountRepository.updateAccountBalance(requestDto.getAccountFromId(), requestDto.getAmount().negate());
        accountRepository.updateAccountBalance(requestDto.getAccountToId(), requestDto.getAmount());
    }

}
