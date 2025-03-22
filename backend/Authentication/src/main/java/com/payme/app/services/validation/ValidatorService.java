package com.payme.app.services.validation;

import com.payme.app.dto.transaction.TransactionRequestDto;
import com.payme.app.entity.Account;

import java.util.List;
import java.util.UUID;

public interface ValidatorService {

    void validateNewTransfer(
            TransactionRequestDto request,
            Account accountFrom,
            List<Account> accountsTo);

    void validateExistingTransfer(UUID transferId);
}
