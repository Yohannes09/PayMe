package com.payme.authentication.controller;

import com.payme.authentication.component.privileged.SigningKeyHandler;
import com.payme.authentication.constant.Endpoints;
import com.payme.authentication.dto.privileged.KeyRotationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequestMapping(Endpoints.SigningKey.BASE)
@RequiredArgsConstructor
public class SigningKeyController {
    private final SigningKeyHandler signingKeyHandler;

    @PostMapping
    public ResponseEntity<Void> rotateSigningKey(@RequestBody @Valid KeyRotationRequest request){
        signingKeyHandler.rotateSigningKey(request);
        return ResponseEntity.noContent().build();
    }

}
