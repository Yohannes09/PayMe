package com.tenmo.controller;

import com.tenmo.dto.AccountDto;
import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.entity.Account;
import com.tenmo.exception.NotFoundException;
import com.tenmo.mapper.AccountMapper;
import com.tenmo.mapper.TransferMapper;
import com.tenmo.services.main.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/tenmo/account")
@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountIds}")
    public ResponseEntity<List<AccountDto>> getAccountById(@PathVariable("accountIds") List<Long> accountIds) {
        List<Account> accounts = accountService.findByAccountId(accountIds)
                .orElseThrow(() -> new NotFoundException("Account(s) not found"));
        return ResponseEntity.ok(AccountMapper.mapAccountToDto(accounts));
    }

    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransferResponseDto>> accountTransferHistory(@PathVariable("accountId") Long accountId) {
        List<TransferResponseDto> transfers = accountService.accountTransferHistory(accountId);

        return transfers.isEmpty() ?
                ResponseEntity.badRequest().build(): ResponseEntity.ok(transfers);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountDto>> getAccounts() {
        return ResponseEntity.ok(
                AccountMapper.mapAccountToDto(accountService.getAccounts())
        );
    }


}
