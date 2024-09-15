package com.techelevator.tenmo.mapper;

import com.techelevator.tenmo.dto.TransferRequestDto;
import com.techelevator.tenmo.entity.Transfer;

import java.util.Optional;

/*
* In an effort to concise the code and abstract some details, this mapper will be used.
* Specifically in lambda expressions to make them easier to read.
* */
public class TransferMapper {
    public static Transfer mapDtoToTransfer(TransferRequestDto requestDto){

        return new Transfer(
                requestDto.getTransferTypeId(),
                requestDto.getTransferStatusId(),
                requestDto.getAccountFromId(),
                requestDto.getAccountToId(),
                requestDto.getAmount(),
                requestDto.getTransferMessage().get(),
                requestDto.getCurrency().get()
        );
    }

    public static TransferRequestDto mapTransferToDto(Transfer request){
        return new TransferRequestDto(
                request.getTransferTypeId(),
                request.getTransferStatusId(),
                request.getAccountFrom(),
                request.getAccountTo(),
                request.getAmount(),
                Optional.ofNullable(request.getTransferMessage()),
                Optional.ofNullable(request.getCurrency())
        );
    }

}
