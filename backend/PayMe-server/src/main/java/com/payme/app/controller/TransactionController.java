package com.payme.app.controller;

import com.payme.app.dto.transaction.TransactionRequestDto;
import com.payme.app.dto.transaction.TransactionResponseDto;
import com.payme.app.entity.Transaction;
import com.payme.app.services.main.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RequestMapping("api/v1/tenmo/transfer")
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }


    @PostMapping("/send")
    public ResponseEntity<TransactionResponseDto> send(@RequestBody @Valid TransactionRequestDto transferRequest){

        return new ResponseEntity<>(transactionService.handleDirectTransfer(transferRequest),
                HttpStatus.CREATED);
    }

    @PostMapping("/request")
    public ResponseEntity<TransactionResponseDto> request(@RequestBody @Valid TransactionRequestDto transferRequest){

        return new ResponseEntity<>(transactionService.handleTransferRequest(transferRequest),
                HttpStatus.CREATED);
    }

//    @GetMapping("/{transferId}")
//    public ResponseEntity<TransferDto> findTransferById(@PathVariable("transferId") UUID transferId) {
//        return ResponseEntity.ok(transferService.findTransferById(transferId));
//    }
//
//    @PostMapping("/transfers")
//    public ResponseEntity<List<TransferDto>> findTransfersById(@RequestParam List<UUID> transferIds) {
//        return ResponseEntity.ok(transferService.findAllByTransferId(transferIds));
//    }

    @PostMapping("/pending/{transferId}/{transferStatusId}")
    public ResponseEntity<Optional<Transaction>> updatePendingTransfer(@PathVariable("transferId") Long transferId,
                                                                       @PathVariable("transferStatusId") Integer newTransferStatusId){

        return null;
    }

}