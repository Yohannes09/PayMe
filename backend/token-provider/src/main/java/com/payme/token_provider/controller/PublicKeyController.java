package com.payme.token_provider.controller;

import com.payme.token_provider.service.SigningKeyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PublicKeyController {
    private final SigningKeyManager signingKeyManager;

    @GetMapping("/public-key")
    public ResponseEntity<String> publicKey(){
        return ResponseEntity.ok(signingKeyManager.getEncodedPublicKey());
    }
}
