package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dto.TransferDto;

import java.util.List;

public interface ClientTransferService {
    List<TransferDto> getTransferByAccountId(int accountId);


}
