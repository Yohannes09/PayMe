package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dto.TransferRequestDto;
import com.techelevator.tenmo.dto.TransferResponseDto;

import java.util.List;
import java.util.Optional;

public interface ClientTransferService {

    Optional<TransferRequestDto> getTransferById(int transferId);
    List<TransferResponseDto> accountTransferHistory(int accountId);


}
