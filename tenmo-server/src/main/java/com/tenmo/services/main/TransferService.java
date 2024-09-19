package com.tenmo.services.main;

import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.entity.Transfer;

import java.util.List;

public interface TransferService {
    Transfer processTransferRequest(TransferRequestDto transferRequest);

    List<Transfer> completeAcceptedTransfers();

    Transfer findTransferById();
}
