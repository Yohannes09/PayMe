package com.payme.token_provider.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${internal.endpoints.refresh-token}")
@Slf4j
public class RefreshTokenController {

    @GetMapping
    public ResponseEntity<String> requestRefreshToken(){
        return ResponseEntity.ok(null);
    }

}
