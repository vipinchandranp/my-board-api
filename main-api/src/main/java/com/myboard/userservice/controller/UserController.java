package com.myboard.userservice.controller;

import com.myboard.userservice.controller.apimodel.*;
import com.myboard.userservice.exception.MyBoardException;
import com.myboard.userservice.service.UserService;
import com.myboard.userservice.types.APIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkFlow flow;

    //USER
    @PostMapping("/signup")
    public MainResponse<UserSignupResponse> signup(@RequestBody UserSignupRequest signupRequest) throws MyBoardException {
        userService.process(signupRequest, APIType.USER_SIGNUP);
        return new MainResponse<>(flow);
    }

    @PostMapping("/login")
    public MainResponse<UserLoginResponse> login(@RequestBody UserLoginRequest loginRequest) throws MyBoardException {
        userService.process(loginRequest, APIType.USER_LOGIN);
        return new MainResponse<>(flow);
    }

}
