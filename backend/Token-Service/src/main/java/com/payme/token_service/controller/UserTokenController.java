package com.payme.token_service.controller;

import com.payme.internal.security.dto.TokenPairResponseDto;
import com.payme.internal.security.dto.UserTokenRequestDto;
import com.payme.internal.security.model.TokenSubject;
import com.payme.token_service.service.UserTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserTokenController {
    private final UserTokenService userTokenService;


    @PostMapping("${internal.endpoints.user.access-and-refresh-token}")
    public ResponseEntity<TokenPairResponseDto> issueAccessAndRefreshToken(@RequestBody @Valid UserTokenRequestDto userTokenRequestDto){
        return ResponseEntity.ok(userTokenService.issueAccessAndRefresh(userTokenRequestDto));
    }

    @PostMapping("${internal.endpoints.user.access-token}")
    public ResponseEntity<String> issueAccessToken(@RequestBody @Valid UserTokenRequestDto userTokenRequestDto){
        return ResponseEntity.ok(userTokenService.issueAccessToken(userTokenRequestDto));
    }

    @PostMapping("${internal.endpoints.user.refresh-token}")
    public ResponseEntity<String> issueRefreshToken(@RequestBody @Valid TokenSubject tokenSubject){
        return ResponseEntity.ok(null);
    }

}
