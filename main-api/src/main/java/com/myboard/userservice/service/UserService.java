package com.myboard.userservice.service;

import com.myboard.userservice.controller.apimodel.*;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.exception.MyBoardException;
import com.myboard.userservice.repository.UserRepository;
import com.myboard.userservice.security.JwtUtil;
import com.myboard.userservice.security.MBAuthManager;
import com.myboard.userservice.types.APIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Service
public class UserService {

    @Autowired
    private WorkFlow flow;

    @Autowired
    private MBAuthManager myBoardAuthManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    public void process(MainRequest baseRequest, APIType apiType) throws MyBoardException {
        try {
            switch (apiType) {
                case USER_SIGNUP:
                    handleUserSignup((UserSignupRequest) baseRequest);
                    break;
                case USER_LOGIN:
                    handleUserLogin((UserLoginRequest) baseRequest);
                    break;
                default:
                    throw new MyBoardException("Invalid API type");
            }
        } catch (Exception e) {
            throw new MyBoardException(e, e.getMessage());
        }
    }

    private void handleUserSignup(UserSignupRequest signupRequest) throws MyBoardException {
        // Check if the user already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            flow.addError(messageSource.getMessage("user.register.usernameExists", null, Locale.getDefault()));
            throw new MyBoardException();
        }

        // Create and save the new user
        User newUser = new User();
        newUser.setUsername(signupRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        newUser.setEmail(signupRequest.getEmail());
        newUser.setFirstName(signupRequest.getFirstName());
        newUser.setLastName(signupRequest.getLastName());
        newUser.setPhone(signupRequest.getPhone());

        userRepository.save(newUser);

        // Prepare the response
        String signUpSuccessMessage = messageSource.getMessage("user.register.success", null, Locale.getDefault());
        flow.addInfo(signUpSuccessMessage);
        flow.setData(null);
    }

    private void handleUserLogin(UserLoginRequest loginRequest) throws MyBoardException {
        Authentication authentication = myBoardAuthManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        String jwtToken = jwtUtil.generateToken(authentication.getName());

        UserLoginResponse loginResponse = new UserLoginResponse();
        loginResponse.setJwtToken(jwtToken);
        flow.setData(loginResponse);
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
