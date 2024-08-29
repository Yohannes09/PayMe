package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dto.TransferHistoryDto;

import java.util.List;

public interface ClientTransferService {
    List<TransferHistoryDto> accountTransferHistory(int accountId);


}
