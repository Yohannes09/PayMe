package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.dto.TransferResponseDtoOLD;

import java.util.List;
import java.util.Optional;

public interface ClientTransferService {

    Optional<TransferResponseDtoOLD> getTransferById(int transferId);
    List<TransferResponseDto> accountTransferHistory(int accountId);


}
