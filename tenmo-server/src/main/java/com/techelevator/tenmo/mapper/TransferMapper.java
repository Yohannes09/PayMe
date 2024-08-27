package com.techelevator.tenmo.mapper;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.model.Transfer;

/*
* In an effort to concise the code and abstract some details, this mapper will be used.
* Specifically in lambda expressions to make them easier to read.
* */
public class TransferMapper {
    public static TransferDto mapTranferToDto(Transfer transfer){
        return new TransferDto(
                transfer.getTransferId(),
                transfer.getSenderAccountId(),
                transfer.getRecipientAccountId(),
                transfer.getTransferStatusId(),
                transfer.getTypeId(),
                transfer.getAmount()
        );
    }

    public static Transfer mapDtoToTransfer(TransferDto dto){
        return new Transfer(
                dto.getTransferId(),
                dto.getSenderAccountId(),
                dto.getRecipientAccountId(),
                dto.getTransferStatusId(),
                dto.getTransferTypeId(),
                dto.getAmount()
        );
    }
}
