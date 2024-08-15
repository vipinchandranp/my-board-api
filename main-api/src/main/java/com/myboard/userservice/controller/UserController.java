package com.myboard.userservice.controller;

import com.myboard.userservice.controller.apimodel.*;
import com.myboard.userservice.exception.MyBoardException;
import com.myboard.userservice.service.UserService;
import com.myboard.userservice.types.APIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserController extends BaseController{

    @Autowired
    private BaseResponse baseResponse;

    @Autowired
    private UserService userService;

    //USER
    @PostMapping("/user/signup")
    public UserSignupResponse signup(UserSignupRequest signupRequest) throws MyBoardException {
        BaseResponse response = userService.process(signupRequest, APIType.USER_SIGNUP);
        UserSignupResponse signupResponse = (UserSignupResponse) response.getData();
        return signupResponse;
    }

    @PostMapping("/user/login")
    public BaseResponse<UserLoginResponse> login(UserLoginRequest loginRequest) throws MyBoardException {
        return userService.process(loginRequest, APIType.USER_LOGIN);
    }

}
