package com.tenmo.services.main;

import com.tenmo.dto.transfer.TransferDto;
import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.dto.transfer.TransferResponseDto;

import java.util.List;
import java.util.UUID;

public interface TransferService {
    TransferDto findTransferById(UUID transferId);

    public List<TransferDto> findAllByTransferId(List<UUID> transferIds);

    public TransferResponseDto handleDirectTransfer(TransferRequestDto request);

    public TransferResponseDto handleTransferRequest(TransferRequestDto request);

    public TransferResponseDto handleApprovedRequest(UUID transferId);

    public TransferResponseDto handleRejectedRequest(UUID transferId);
}
