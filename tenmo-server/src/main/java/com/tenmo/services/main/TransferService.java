package com.tenmo.services.main;

import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.entity.Transfer;

public interface TransferService {
    Transfer findTransferById(Long transferId);

    public TransferResponseDto handleDirectTransfer(TransferRequestDto request);

    public TransferResponseDto handleTransferRequest(TransferRequestDto request);

    public TransferResponseDto handleApprovedRequest(Long transferId);

    public TransferResponseDto handleRejectedRequest(Long transferId);
}
