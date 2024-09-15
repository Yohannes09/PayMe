package com.techelevator.tenmo.services.main;

import com.techelevator.tenmo.dto.TransferRequestDto;
import com.techelevator.tenmo.exception.NotFoundException;
import com.techelevator.tenmo.entity.Transfer;
import com.techelevator.tenmo.mapper.TransferMapper;
import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.repository.TransferRepository;
import com.techelevator.tenmo.services.utils.TransferStatus;
import com.techelevator.tenmo.services.utils.TransferType;
import com.techelevator.tenmo.services.validation.ValidatorService;
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
                .equals(TransferType.SEND.getTransferTypeId()))
            handleAccountBalances(transferRequest);

        return Optional.ofNullable(
                transferRepository.save(TransferMapper.mapDtoToTransfer(transferRequest))
        ).orElseThrow(() -> new NotFoundException(""));
    }

    @Override
    public List<Transfer> processAcceptedTransfers(){
        List<Transfer> approvedPendingTransfers =  transferRepository
                .findAll()
                .stream()
                .filter(transfer -> {
                     return transfer.getTransferStatusId().equals(TransferStatus.PENDING.getStatusId()) &&
                             transfer.getTransferTypeId().equals(TransferType.REQUEST.getTransferTypeId());
                }).collect(Collectors.toList());

        approvedPendingTransfers.forEach(transfer -> {
            handleAccountBalances(TransferMapper.mapTransferToDto(transfer));
            updatePendingTransfer(transfer.getTransferId(), TransferStatus.COMPLETED.getStatusId());
        });

        return approvedPendingTransfers;
    }


    private Optional<Transfer> updatePendingTransfer(Long transferId, Integer newTransferStatusId) {
        return Optional.ofNullable(transferRepository.updateTransferStatus(transferId, newTransferStatusId)
        ).orElseThrow(() -> new NotFoundException(""));
    }

    private void handleAccountBalances(TransferRequestDto requestDto){
        accountRepository.updateBalance(requestDto.getAccountFromId(), requestDto.getAmount().negate());
        accountRepository.updateBalance(requestDto.getAccountToId(), requestDto.getAmount());
    }

}
