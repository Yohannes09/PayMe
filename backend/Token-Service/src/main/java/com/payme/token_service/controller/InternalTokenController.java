package com.payme.token_service.controller;

import com.payme.token_service.dto.InternalTokenDto;
import com.payme.token_service.dto.TokenPairDto;
import com.payme.token_service.service.InternalTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InternalTokenController {
    private final InternalTokenService internalTokenService;


    @PostMapping("${internal.endpoints.service.access-and-refresh-token}")
    public ResponseEntity<TokenPairDto> issueAccessAndRefreshToken(
            @RequestBody @Valid InternalTokenDto internalTokenDto
    ){
        return ResponseEntity.ok(internalTokenService.issueAccessAndRefresh(
                internalTokenDto.serviceNameOrId())
        );
    }

    @PostMapping("${internal.endpoints.service.access-token}")
    public ResponseEntity<String> issueAccessToken(
            @RequestBody @Valid InternalTokenDto internalTokenDto
    ){
        return ResponseEntity.ok(internalTokenService.issueAccessToken(
                internalTokenDto.serviceNameOrId())
        );
    }

    @PostMapping("${internal.endpoints.service.refresh-token}")
    public ResponseEntity<String> issueRefreshToken(
            String serviceNameOrId
    ){
        return ResponseEntity.ok(null);
    }

}
