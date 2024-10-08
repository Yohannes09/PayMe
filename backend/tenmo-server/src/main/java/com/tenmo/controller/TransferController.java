package com.tenmo.controller;

import com.tenmo.dto.transfer.TransferDto;
import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.entity.Transfer;
import com.tenmo.services.main.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RequestMapping("api/v1/tenmo/transfer")
@RestController
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService){
        this.transferService = transferService;
    }


    @PostMapping("/send")
    public ResponseEntity<TransferResponseDto> send(@RequestBody @Valid TransferRequestDto transferRequest){

        return new ResponseEntity<>(transferService.handleDirectTransfer(transferRequest),
                HttpStatus.CREATED);
    }

    @PostMapping("/request")
    public ResponseEntity<TransferResponseDto> request(@RequestBody @Valid TransferRequestDto transferRequest){

        return new ResponseEntity<>(transferService.handleTransferRequest(transferRequest),
                HttpStatus.CREATED);
    }

    @GetMapping("/{transferId}")
    public ResponseEntity<TransferDto> findTransferById(@PathVariable("transferId") UUID transferId) {
        return ResponseEntity.ok(transferService.findTransferById(transferId));
    }

    @PostMapping("/transfers")
    public ResponseEntity<List<TransferDto>> findTransfersById(@RequestParam List<UUID> transferIds) {
        return ResponseEntity.ok(transferService.findAllByTransferId(transferIds));
    }

    @PostMapping("/pending/{transferId}/{transferStatusId}")
    public ResponseEntity<Optional<Transfer>> updatePendingTransfer(@PathVariable("transferId") Long transferId,
                                                                    @PathVariable("transferStatusId") Integer newTransferStatusId){

        return null;
    }

}