package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.model.TransferStatus;

import java.util.List;
import java.util.Optional;

public interface TransferStatusRepository {
    Optional<TransferStatus> createTransferStatus(String description);
    Optional <TransferStatus> findTransferStatusById(int id);
    Optional <List<TransferStatus>> getAllTransferStatus();
    int deleteTransferStatus(int id);
}
