package com.tenmo.services.main;

import com.tenmo.dto.transfer.TransferDto;
import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.exception.BadRequestException;
import com.tenmo.exception.NotFoundException;
import com.tenmo.entity.Transfer;
import com.tenmo.mapper.TransferMapper;
import com.tenmo.repository.AccountRepository;
import com.tenmo.repository.TransferRepository;
import com.tenmo.repository.TransferStatusRepository;
import com.tenmo.repository.TransferTypeRepository;
import com.tenmo.services.validation.ValidatorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class RestTransferService implements TransferService {
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final ValidatorService validatorService;
    private final TransferTypeRepository typeRepository;
    private final TransferStatusRepository statusRepository;

    public RestTransferService(
            AccountRepository accountRepository,
            TransferRepository transferRepository,
            ValidatorService validatorService,
            TransferTypeRepository typeRepository,
            TransferStatusRepository statusRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.validatorService = validatorService;
        this.typeRepository = typeRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public Transfer findTransferById(Long transferId) {
        return transferRepository.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Could not find a tranfer with ID: " + transferId));
    }

    @Override
    public Transfer handleDirectTransfer(TransferRequestDto request){
        Transfer newTransfer = createTransfer(request, "send", "completed");
        accountRepository.handleDirectTransfer(request.getAccountFromId(), request.getAccountToId(), request.getAmount());
        return transferRepository.save(newTransfer);
    }

    @Override
    public Transfer handleTransferRequest(TransferRequestDto request){
        Transfer newTransfer = createTransfer(request, "request", "pending");
        return transferRepository.save(newTransfer);
    }

//    public TransferDto handleApprovedTransferRequest(Long tranferId){
//        Transfer transfer = updatePendingTransfer(tranferId, getStatusId("completed"));
//        accountRepository.handleDirectTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
//        return
//    }
//
//    public TransferDto handleDeclinedTransferRequest(Long transferId){
//
//    }

    private Transfer updatePendingTransfer(Long transferId, Integer newStatusId) {
        //validatorService.validateExistingTransfer();
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer with ID: " + transferId + " could not be found. "));
        transferRepository.updateTransferStatus(transfer.getTransferId(), newStatusId);
        return transfer;
    }

    private Transfer createTransfer(TransferRequestDto request, String typeDescription, String statusDescription){
        validatorService.validateNewTransfer(request);
        Transfer newTransfer = TransferMapper.mapDtoToTransfer(request);
        newTransfer.setTypeId(getTypeId(typeDescription));
        newTransfer.setStatusId(getStatusId(statusDescription));
        return newTransfer;
    }

    private Integer getTypeId(String typeDescription){
        return typeRepository.findByDescription(typeDescription)
                .orElseThrow(() -> new BadRequestException("Invalid transfer type: " + typeDescription))
                .getTypeId();
    }

    private Integer getStatusId(String statusDescription){
        return statusRepository.findByDescription(statusDescription)
                .orElseThrow(() -> new BadRequestException("Invalid transfer status: " + statusDescription))
                .getStatusId();
    }

}
