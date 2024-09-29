package com.tenmo.services.main;

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
import com.tenmo.util.TransferStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Transfer findTransferById(Long transferId) {
        return transferRepository.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Could not find a tranfer with ID: " + transferId));
    }


    /** @param  */

    private TransferResponseDto processTransferRequest(
            TransferRequestDto request,
            TransferTypeEnum type,
            TransferStatusEnum status){

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
                TransferStatusEnum.COMPLETED);

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
                TransferStatusEnum.PENDING);
    }



    private Transfer updatePendingTransfer(Long transferId, TransferStatusEnum newStatus) {
        validatorService.validateExistingTransfer(transferId);

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer with ID: " + transferId + " could not be found. "));

        transfer.setStatusId(newStatus.getId());

        return transferRepository.save(transfer);
    }

    @Override
    public TransferResponseDto handleApprovedRequest(Long transferId){
        Transfer transfer = updatePendingTransfer(
                transferId,
                TransferStatusEnum.COMPLETED);

        accountRepository.handleDirectTransfer(
                transfer.getAccountFrom(),
                transfer.getAccountTo(),
                transfer.getAmount());

        return transferRepository.transferResponse(transfer.getTransferId())
                .orElseThrow(() -> new NotFoundException("Transfer response not found with ID : " + transferId));
    }

    @Override
    public TransferResponseDto handleRejectedRequest(Long transferId){
        updatePendingTransfer(
                transferId,
                TransferStatusEnum.REJECTED);

        return transferRepository.transferResponse(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer response not found with ID : " + transferId));
    }

}
