package com.payme.token_service.controller;

import com.payme.token_service.component.signing_key.SigningKeyManager;
import com.payme.token_service.model.RecentPublicKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${internal.endpoints.public-key}")
@Slf4j
public class PublicKeyController {
    private final SigningKeyManager signingKeyManager;

    @GetMapping
    public ResponseEntity<RecentPublicKeys> publicKey(){
        return ResponseEntity.ok(signingKeyManager.getCurrentAndPreviousPublicKeys());
    }
}
