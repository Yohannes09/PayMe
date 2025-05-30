package com.payme.authentication.controller;

import com.payme.authentication.component.RoleProvider;
import com.payme.authentication.entity.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/dummy")
@RequiredArgsConstructor
public class DummyController {
    private final RoleProvider roleProvider;


    @GetMapping("/{role}")
    public ResponseEntity<Role> getRole(@PathVariable String role){
        log.info("fetching role...");
        return ResponseEntity.ok(roleProvider.findRole(role));
    }

}
