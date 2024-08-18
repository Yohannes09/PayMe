package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("api/v1/transfer")
@RestController
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }


    @PostMapping("")
    public ResponseEntity<TransferResponseDto> transfer(@Valid @RequestBody TransferDto transferDto){

        Optional<Transfer> transfer = transferService.processTransfer(
                transferDto.getAccountSenderId(),
                transferDto. getAccountRecipientId(),
                transferDto.getAmount()
        );

        //HORRIBLE convention. ActSender?? Stay consistent.
        if(transfer.isPresent()) {
            Transfer clientResponse = transferService.getTransferById(transfer.get().getTransferId()).get();
            TransferResponseDto transferResponseDto = new TransferResponseDto(
                    clientResponse.getActSenderId(),
                    clientResponse.getActRecipientId(),
                    clientResponse.getAmount()
            );
            return new ResponseEntity<>(transferResponseDto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
