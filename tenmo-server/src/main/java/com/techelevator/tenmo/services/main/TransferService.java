package com.techelevator.tenmo.services.main;

import com.techelevator.tenmo.dto.TransferRequestDto;
import com.techelevator.tenmo.entity.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferService {
    Transfer processTransferRequest(TransferRequestDto transferRequest);

    List<Transfer> completeAcceptedTransfers();

    Transfer findTransferById();
}
