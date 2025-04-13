package com.payme.token_provider.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class TokenController {

    @GetMapping("/access-token")
    public ResponseEntity<String> requestAccessToken(){
        return ResponseEntity.ok(null);
    }


    @GetMapping
    public ResponseEntity<String> requestRefreshToken(){
        return ResponseEntity.ok(null);
    }
}
