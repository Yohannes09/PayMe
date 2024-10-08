package com.tenmo.mapper;

import com.tenmo.dto.transfer.TransferDto;
import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.entity.Transfer;

import java.util.Optional;

public class TransferMapper {

    public static Transfer mapRequestToTransfer(TransferRequestDto requestDto){

        return new Transfer();
//                requestDto.getAccountFromId(),
//                requestDto.getAccountToId(),
//                requestDto.getAmount(),
//                requestDto.getTransferMessage().get(),
//                requestDto.getCurrency().get()
//        );
    }

    public static TransferRequestDto mapTransferToRequest(Transfer request){
        return new TransferRequestDto();
//                request.getAccountFrom(),
//                request.getAccountTo(),
//                request.getAmount(),
//                Optional.ofNullable(request.getTransferMessage()),
//                Optional.ofNullable(request.getCurrency())
//        );
    }

    public static TransferDto mapTransferToDto(Transfer transfer){
        return new TransferDto();
//                transfer.getTransferId(),
//                transfer.getAccountFrom(),
//                transfer.getAccountTo(),
//                transfer.getAmount(),
//                Optional.ofNullable(transfer.getTransferMessage()),
//                Optional.ofNullable(transfer.getCurrency()),
//                transfer.getCreatedAt()
//        );
    }


}
