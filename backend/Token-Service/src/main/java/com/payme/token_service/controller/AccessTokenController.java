package com.payme.token_provider.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${internal.endpoints.access-token}")
@RequiredArgsConstructor
@Slf4j
public class AccessTokenController {

    @GetMapping
    public ResponseEntity<String> requestAccessToken(){
        return ResponseEntity.ok(null);
    }

}
