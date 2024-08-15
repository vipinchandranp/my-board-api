package com.myboard.userservice.service;

import com.myboard.userservice.controller.apimodel.BaseRequest;
import com.myboard.userservice.controller.apimodel.BaseResponse;
import com.myboard.userservice.controller.apimodel.UserSignupRequest;
import com.myboard.userservice.controller.apimodel.UserLoginResponse;
import com.myboard.userservice.types.APIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.myboard.userservice.entity.User;

@Service
public class BaseService {

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
