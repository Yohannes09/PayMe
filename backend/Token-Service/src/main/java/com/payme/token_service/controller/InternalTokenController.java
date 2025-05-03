package com.payme.token_service.controller;

import com.payme.token_service.dto.TokenPairDto;
import com.payme.token_service.service.InternalTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InternalTokenController {
    private final InternalTokenService internalTokenService;


    @PostMapping("${internal.endpoints.service.access-and-refresh-token}")
    public ResponseEntity<TokenPairDto> issueAccessAndRefreshToken(String usernameOrId){
        return ResponseEntity.ok(internalTokenService.issueAccessAndRefresh(usernameOrId));
    }

    @PostMapping("${internal.endpoints.service.access-token}")
    public ResponseEntity<String> issueAccessToken(String usernameOrId){
        return ResponseEntity.ok(internalTokenService.issueAccessToken(usernameOrId));
    }

    @PostMapping("${internal.endpoints.service.refresh-token}")
    public ResponseEntity<String> issueRefreshToken(String usernameOrId){
        return ResponseEntity.ok(null);
    }

}
