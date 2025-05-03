package com.payme.token_service.controller;

import com.payme.token_service.dto.TokenPairDto;
import com.payme.token_service.model.TokenSubject;
import com.payme.token_service.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserTokenController {
    private final UserTokenService userTokenService;


    @PostMapping("${internal.endpoints.user.access-and-refresh-token}")
    public ResponseEntity<TokenPairDto> issueAccessAndRefreshToken(TokenSubject tokenSubject){
        return ResponseEntity.ok(userTokenService.issueAccessAndRefresh(tokenSubject));
    }

    @PostMapping("${internal.endpoints.user.access-token}")
    public ResponseEntity<String> issueAccessToken(TokenSubject tokenSubject){
        return ResponseEntity.ok(null);
    }

    @PostMapping("${internal.endpoints.user.refresh-token}")
    public ResponseEntity<String> issueRefreshToken(TokenSubject tokenSubject){
        return ResponseEntity.ok(null);
    }

}
