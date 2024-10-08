package com.tenmo.controller;

import com.tenmo.dto.account.AccountDto;
import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.services.main.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("api/v1/tenmo/account")
@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    /*  GET api/.../history/123?transferType=sent
    *   GET api/.../history/123?transferType=sent&status=pending
    *   GET api/.../history/123?status=pending*/
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransferResponseDto>> accountTransferHistory(
            @PathVariable("accountId") Long accountId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status) {

        List<TransferResponseDto> transfers =
                accountService.accountTransferHistory(
                        accountId,
                        Optional.ofNullable(type),
                        Optional.ofNullable(status));

        return ResponseEntity.ok(transfers);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountDto>> getAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

}
