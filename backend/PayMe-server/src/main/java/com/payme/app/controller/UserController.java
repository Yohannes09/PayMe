package com.payme.app.controller;

import com.payme.app.dto.user.UserDto;
import com.payme.app.services.main.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5500"})
@RequestMapping("api/v1/user")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
//@PreAuthorize("hasRole('ADMIN')")


    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getAccountByid(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

}
