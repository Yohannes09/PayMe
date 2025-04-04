package com.payme.authentication.diffMicroServ.controller;

import com.payme.authentication.diffMicroServ.dto.AccountDto;
import com.payme.authentication.diffMicroServ.dto.TransactionResponseDto;
import com.payme.authentication.diffMicroServ.service.main.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("api/v1/account")
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
    public ResponseEntity<List<TransactionResponseDto>> accountTransferHistory(
            @PathVariable("accountId") UUID accountId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status) {

        List<TransactionResponseDto> transfers =
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
