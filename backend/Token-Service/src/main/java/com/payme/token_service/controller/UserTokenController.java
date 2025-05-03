package com.payme.token_service.controller;

import com.payme.token_service.dto.TokenPairDto;
import com.payme.token_service.dto.UserTokenDto;
import com.payme.token_service.model.TokenSubject;
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
    public ResponseEntity<TokenPairDto> issueAccessAndRefreshToken(@RequestBody @Valid UserTokenDto userTokenDto){
        return ResponseEntity.ok(userTokenService.issueAccessAndRefresh(userTokenDto));
    }

    @PostMapping("${internal.endpoints.user.access-token}")
    public ResponseEntity<String> issueAccessToken(@RequestBody @Valid UserTokenDto userTokenDto){
        return ResponseEntity.ok(userTokenService.issueAccessToken(userTokenDto));
    }

    @PostMapping("${internal.endpoints.user.refresh-token}")
    public ResponseEntity<String> issueRefreshToken(@RequestBody @Valid TokenSubject tokenSubject){
        return ResponseEntity.ok(null);
    }

}
