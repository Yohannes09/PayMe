package com.techelevator.tenmo.services.validation;

import com.techelevator.tenmo.dto.TransferRequestDto;

// explore ways to make this generic. The service should have the ability to validate multiple services.
public interface ValidatorService {
    void validateTransferRequest(TransferRequestDto request);
}
