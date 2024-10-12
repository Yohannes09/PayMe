package com.tenmo.app.services.main;

import com.tenmo.app.dto.transfer.TransferRequestDto;
import com.tenmo.app.dto.transfer.TransferResponseDto;

import java.util.List;
import java.util.UUID;

public interface TransferService {

    List<TransferResponseDto> findAllByTransferId(List<UUID> transferIds);

    TransferResponseDto handleDirectTransfer(TransferRequestDto request);

    TransferResponseDto handleTransferRequest(TransferRequestDto request);

    TransferResponseDto handleApprovedRequest(UUID transferId);

    TransferResponseDto handleRejectedRequest(UUID transferId);
}
