package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.user.UserLoginRequest;
import com.myboard.userservice.controller.model.user.UserLoginResponse;
import com.myboard.userservice.controller.model.user.UserSignupRequest;
import com.myboard.userservice.controller.model.user.UserSignupResponse;
import com.myboard.userservice.exception.MBException;
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
    public MainResponse<UserSignupResponse> signup(@RequestBody UserSignupRequest signupRequest) throws MBException {
        userService.process(signupRequest, APIType.USER_SIGNUP);
        return new MainResponse<>(flow);
    }

    @PostMapping("/login")
    public MainResponse<UserLoginResponse> login(@RequestBody UserLoginRequest loginRequest) throws MBException {
        userService.process(loginRequest, APIType.USER_LOGIN);
        return new MainResponse<>(flow);
    }

}
