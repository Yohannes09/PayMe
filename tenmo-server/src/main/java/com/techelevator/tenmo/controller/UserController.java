package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dto.AccountDto;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.RestUserService;
import com.techelevator.tenmo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.Optional;

@RequestMapping("api/tenmo/user")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserController() {
        this.userService = new RestUserService();
    }


    // Ideally would need a Dto but it's crunch time.
    @GetMapping("/{userId}")
    public ResponseEntity<User> getAccountByid(@PathVariable("userId") int userId) {
        Optional<User> user = userService.getUserById(userId);

        if (user.isPresent()) {

            return new ResponseEntity<>(new User(
                    user.get().getId(),
                    user.get().getUsername(),
                    user.get().getPassword(),
                    "This feature does not work"), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
