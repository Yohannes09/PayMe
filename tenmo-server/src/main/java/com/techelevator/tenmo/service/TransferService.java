package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.entity.Transfer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransferService {
    Optional<Transfer> processTransfer(Integer transferTypeId, Long accountFromId, Long accountToId, BigDecimal amount, String transferMessage);


    // Obtain a list of an accounts transfer history.
    List<TransferResponseDto> getAccountHistory(Long accountId);
    Optional<Transfer> getTransferById(Long transferId);
    List<TransferResponseDto> accountTransferStatus(Integer transferStatusId, Long accountId);

    Optional<Transfer> updatePendingTransfer(Long transferId, Integer newTransferStatusId);
}
