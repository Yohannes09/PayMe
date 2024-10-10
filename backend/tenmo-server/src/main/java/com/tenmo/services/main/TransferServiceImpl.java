package com.tenmo.services.main;

import com.tenmo.dto.transfer.TransferDto;
import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.util.TransferType;
import com.tenmo.exception.BadRequestException;
import com.tenmo.exception.NotFoundException;
import com.tenmo.entity.Transfer;
import com.tenmo.mapper.TransferMapper;
import com.tenmo.repository.AccountRepository;
import com.tenmo.repository.TransferRepository;
import com.tenmo.services.validation.ValidatorService;
import com.tenmo.util.TransferStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class TransferServiceImpl implements TransferService {
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
    public TransferDto findTransferById(UUID transferId) {
        return transferRepository
                .findById(transferId)
                .map(TransferMapper::mapTransferToDto)
                .orElseThrow(()-> new NotFoundException("Transfer " + transferId + " not found. "));
    }

    @Override
    public List<TransferDto> findAllByTransferId(List<UUID> transferIds){
        return transferRepository
                .findAllById(transferIds)
                .stream()
                .map(TransferMapper::mapTransferToDto)
                .collect(Collectors.toList()
        );
    }

    @Override
    public TransferResponseDto handleDirectTransfer(TransferRequestDto request){
        TransferResponseDto newTransfer = processTransferRequest(
                request,
                TransferType.SEND,
                TransferStatus.COMPLETED);

        accountRepository.handleDirectTransfer(
                request.accountFromId(),
                request.accountToId(),
                request.amount());

        return newTransfer;
    }

    @Override
    public TransferResponseDto handleTransferRequest(TransferRequestDto request){
        return processTransferRequest(
                request,
                TransferType.REQUEST,
                TransferStatus.PENDING
        );
    }


    private TransferResponseDto processTransferRequest(
            TransferRequestDto request,
            TransferType type,
            TransferStatus status){
        validatorService.validateNewTransfer(request);

        Transfer newTransfer = TransferMapper.mapRequestToTransfer(request);
        newTransfer.setType(type);
        newTransfer.setStatus(status);
        transferRepository.save(newTransfer);

        return transferRepository.transferResponse(newTransfer.getTransferId())
                .orElseThrow(()-> new BadRequestException("An issue occured with your transfer "));
    }


    private Transfer updatePendingTransfer(UUID transferId, TransferStatus newStatus) {
        validatorService.validateExistingTransfer(transferId);

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer with ID: " + transferId + " could not be found. "));

        transfer.setStatus(newStatus);

        return transferRepository.save(transfer);
    }

    @Override
    public TransferResponseDto handleApprovedRequest(UUID transferId){
        Transfer transfer = updatePendingTransfer(
                transferId,
                TransferStatus.COMPLETED);

//        accountRepository.handleDirectTransfer(
//                transfer.getAccountFrom(),
//                transfer.getAccountTo(),
//                transfer.getAmount());

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

}
