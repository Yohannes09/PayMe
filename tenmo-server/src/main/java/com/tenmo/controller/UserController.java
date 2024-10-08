package com.tenmo.controller;

import com.tenmo.dto.user.UserDto;
import com.tenmo.services.main.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("api/v1/tenmo/user")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getAccountByid(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }
}
