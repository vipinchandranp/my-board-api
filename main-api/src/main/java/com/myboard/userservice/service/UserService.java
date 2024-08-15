package com.myboard.userservice.service;

import com.myboard.userservice.controller.apimodel.*;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.exception.MyBoardException;
import com.myboard.userservice.security.JwtUtil;
import com.myboard.userservice.security.MyBoardAuthManager;
import com.myboard.userservice.types.APIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService {

    @Autowired
    private BaseResponse baseResponse;

    @Autowired
    private MyBoardAuthManager myBoardAuthManager;

    @Autowired
    private JwtUtil jwtUtil;

    public BaseResponse process(BaseRequest baseRequest, APIType apiType) throws MyBoardException {
        try {
            switch (apiType) {
                case USER_SIGNUP:
                    break;
                case USER_LOGIN:
                    UserLoginRequest loginRequest = (UserLoginRequest) baseRequest;
                    Authentication authentication = myBoardAuthManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getUsername(),
                                    loginRequest.getPassword()
                            )
                    );
                    String jwtToken = jwtUtil.generateToken(authentication.getName());
                    UserLoginResponse loginResponse = new UserLoginResponse();
                    loginResponse.setJwtToken(jwtToken);
                    baseResponse.setData(loginResponse);
                    break;
                default:
                    break;
            }
            return baseResponse;
        } catch (Exception e) {
            throw new MyBoardException(e);
        }
    }

}
