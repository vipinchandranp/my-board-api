package com.myboard.userservice.controller;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Define API endpoints for user management operations
    // ...
}

