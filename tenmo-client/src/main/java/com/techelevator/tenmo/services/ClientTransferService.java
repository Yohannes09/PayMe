package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.dto.TransferHistoryDto;
import com.techelevator.tenmo.dto.TransferResponseDto;

import java.util.List;
import java.util.Optional;

public interface ClientTransferService {

    Optional<TransferResponseDto> getTransferById(int transferId);
    List<TransferHistoryDto> accountTransferHistory(int accountId);


}
