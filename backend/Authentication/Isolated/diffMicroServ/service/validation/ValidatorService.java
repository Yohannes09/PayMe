package com.payme.authentication.diffMicroServ.service.validation;

import com.payme.authentication.diffMicroServ.dto.TransactionRequestDto;
import com.payme.authentication.diffMicroServ.entity.Account;

import java.util.List;
import java.util.UUID;

public interface ValidatorService {

    void validateNewTransfer(
            TransactionRequestDto request,
            Account accountFrom,
            List<Account> accountsTo);

    void validateExistingTransfer(UUID transferId);
}
