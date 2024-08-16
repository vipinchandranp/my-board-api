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
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private MyBoardWorkFlow flow;

    //USER
    @PostMapping("/signup")
    public MyBoardResponse<UserSignupResponse> signup(@RequestBody UserSignupRequest signupRequest) throws MyBoardException {
        userService.process(signupRequest, APIType.USER_SIGNUP);
        return new MyBoardResponse<>(flow);
    }

    @PostMapping("/login")
    public MyBoardResponse<UserLoginResponse> login(@RequestBody UserLoginRequest loginRequest) throws MyBoardException {
        userService.process(loginRequest, APIType.USER_LOGIN);
        return new MyBoardResponse<>(flow);
    }

}
