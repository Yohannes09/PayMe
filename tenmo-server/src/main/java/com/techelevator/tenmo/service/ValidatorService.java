package com.techelevator.tenmo.service;

import com.techelevator.tenmo.entity.Transfer;
import com.techelevator.tenmo.exception.AccountException;
import com.techelevator.tenmo.exception.TransferException;

import java.math.BigDecimal;
// explore ways to make this generic. The service should have the ability to validate multiple services.
public interface ValidatorService {

//    private void validateTransfer
//            (Long transferId, Integer transferTypeId, Long accountFromId, Long accountToId, BigDecimal amount) throws TransferException, AccountException {
//
//        if(accountFromId.equals(accountToId))
//            throw new TransferException("Sender and recipient IDs cannot be the same. ");
//
//        if(amount.compareTo(BigDecimal.ZERO) <= 0)
//            throw new TransferException("Amount must be greater than zero. ");
//
//        if(!TransferType.validateTypeId(transferTypeId))
//            throw new TransferException("Invalid transfer type ID. ");
//
//        if(!accountRepository.existsById(accountFromId) || !accountRepository.existsById(accountToId))
//            throw new AccountException("One or both accounts do not exist. ");
//
//        if(transferId != null){
//            if(!transferRepository.existsById(transferId))
//                throw new TransferException("Transfer does not exist. ");
//
//            Transfer transfer = transferRepository.findTransferById(transferId);
//            Integer currTypeId = transfer.getTransferTypeId();
//            Integer currStatusId = transfer.getTransferStatusId();
//
//            if (currTypeId == TransferType.REQUEST.getTransferTypeId() && currStatusId != TransferStatus.PENDING.getTransferStatusId()) {
//                throw new TransferException("Transfer already processed");
//            }
//
//        }
//
//    }
}
