package com.tenmo.controller;

import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.entity.Transfer;
import com.tenmo.services.main.AccountService;
import com.tenmo.services.main.TransferService;
import com.tenmo.services.main.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RequestMapping("api/tenmo/transfer")
@RestController
public class TransferController {
    private final TransferService transferService;
    private final AccountService accountService;
    private final UserService userService;

    public TransferController(TransferService transferService, AccountService accountService, UserService userService) {
        this.transferService = transferService;
        this.accountService = accountService;
        this.userService = userService;
    }





    public ResponseEntity<Transfer> findTransferById(@PathVariable("transferId") Long transferId) {
        return ResponseEntity.ok(transferService.findTransferById(transferId));
    }


    // Maybe this should be approvePendingTransfer maybe deny shoud be here.
    // client shouldn't be able to update the status directly
    // just approve and deny a transer
    @PostMapping("/pending/{transferId}/{transferStatusId}")
    public ResponseEntity<Optional<Transfer>> updatePendingTransfer(@PathVariable("transferId") Long transferId,
                                                                    @PathVariable("transferStatusId") Integer newTransferStatusId){

        return null;
    }

}