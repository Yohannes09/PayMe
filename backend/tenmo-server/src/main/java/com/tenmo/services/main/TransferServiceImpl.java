package com.tenmo.services.main;

import com.tenmo.dto.transfer.TransferDto;
import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.util.TransferTypeEnum;
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
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Could not find a tranfer with ID: " + transferId));

        return TransferMapper.mapTransferToDto(transfer);
    }

    @Override
    public List<TransferDto> findAllByTransferId(List<UUID> transferIds){
        List<Transfer> transfers = transferRepository.findAllById(transferIds);
        //transfer -> TransferMapper.mapTransferToDto(transfer)
        return transfers
                .stream()
                .map(TransferMapper::mapTransferToDto)
                .collect(Collectors.toList());
    }



    private TransferResponseDto processTransferRequest(
            TransferRequestDto request,
            TransferTypeEnum type,
            TransferStatus status){

        validatorService.validateNewTransfer(request);

        Transfer newTransfer = TransferMapper.mapRequestToTransfer(request);
        newTransfer.setTypeId(type.getId());
        newTransfer.setStatusId(status.getId());

        transferRepository.save(newTransfer);

        return transferRepository.transferResponse(newTransfer.getTransferId()).
                orElseThrow(() -> new BadRequestException("An issue occured with your transfer "));
    }

    @Override
    public TransferResponseDto handleDirectTransfer(TransferRequestDto request){
        TransferResponseDto newTransfer = processTransferRequest(
                request,
                TransferTypeEnum.SEND,
                TransferStatus.COMPLETED);

        accountRepository.handleDirectTransfer(
                request.getAccountFromId(),
                request.getAccountToId(),
                request.getAmount());

        return newTransfer;
    }

    @Override
    public TransferResponseDto handleTransferRequest(TransferRequestDto request){
        return processTransferRequest(
                request,
                TransferTypeEnum.REQUEST,
                TransferStatus.PENDING);
    }



    private Transfer updatePendingTransfer(UUID transferId, TransferStatus newStatus) {
        validatorService.validateExistingTransfer(transferId);

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer with ID: " + transferId + " could not be found. "));

        transfer.setStatusId(newStatus.getId());

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
