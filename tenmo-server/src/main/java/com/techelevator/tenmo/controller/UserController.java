package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.entity.User;
import com.techelevator.tenmo.services.main.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("api/tenmo/user")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{userId}")
    public ResponseEntity<User> getAccountByid(@PathVariable("userId") Long userId) {
        Optional<User> user = userService.getUserById(userId);

        if (user.isPresent()) {

            return new ResponseEntity<>(new User(
                    user.get().getUserId(),
                    user.get().getUsername(),
                    user.get().getPasswordHash(),
                    user.get().getEmail(),
                    user.get().getRole(),
                    user.get().isActive(),
                    user.get().getCreatedAt(),
                    user.get().getAccounts()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
