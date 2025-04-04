package com.payme.authentication.diffMicroServ.controller;

import com.payme.authentication.diffMicroServ.dto.TransactionRequestDto;
import com.payme.authentication.diffMicroServ.dto.TransactionResponseDto;
import com.payme.authentication.diffMicroServ.entity.Transaction;
import com.payme.authentication.diffMicroServ.service.main.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5500"})
@RequestMapping("api/v1/transfer")
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

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<TransactionResponseDto>> getAllUserTransactions(@PathVariable UUID userId){
        return new ResponseEntity<>(transactionService.getAllUserTransactions(userId), HttpStatus.OK);
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