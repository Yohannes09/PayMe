package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.service.RestUserService;
import com.techelevator.tenmo.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
