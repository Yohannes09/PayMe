package com.tenmo.services.validation;

import com.tenmo.dto.transfer.TransferRequestDto;

// explore ways to make this generic. The service should have the ability to validate multiple services.
public interface ValidatorService {
    void validateNewTransfer(TransferRequestDto request);

    //void validateExistingTransfer(Long transferId);
}
