package com.tenmo.app.services.validation;

import com.tenmo.app.dto.transfer.TransferRequestDto;
import com.tenmo.app.entity.Account;

import java.util.List;
import java.util.UUID;

public interface ValidatorService {

    void validateNewTransfer(
            TransferRequestDto request,
            Account accountFrom,
            List<Account> accountsTo);

    void validateExistingTransfer(UUID transferId);
}
