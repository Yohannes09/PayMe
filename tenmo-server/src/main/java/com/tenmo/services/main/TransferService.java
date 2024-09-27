package com.tenmo.services.main;

import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.entity.Transfer;

import java.util.List;

public interface TransferService {
    Transfer findTransferById(Long transferId);

    public Transfer handleDirectTransfer(TransferRequestDto request);

    public Transfer handleTransferRequest(TransferRequestDto request);
}
