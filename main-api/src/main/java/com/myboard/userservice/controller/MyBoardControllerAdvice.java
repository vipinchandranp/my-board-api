package com.myboard.userservice.controller;

import com.myboard.userservice.controller.apimodel.MyBoardRequest;
import com.myboard.userservice.controller.apimodel.MyBoardResponse;
import com.myboard.userservice.controller.apimodel.MyBoardWorkFlow;
import com.myboard.userservice.exception.MyBoardException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class MyBoardControllerAdvice {

    @Autowired
    private MessageSource messageSource;


    @Autowired
    private MyBoardWorkFlow myBoardWorkFlow;

    @ExceptionHandler(MyBoardException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MyBoardResponse handleMyBoardException(MyBoardException ex) {
        return new MyBoardResponse(myBoardWorkFlow);
    }

    @ExceptionHandler(Exception.class)
    public MyBoardResponse handleGlobalException(Exception ex) {
        String internalError = messageSource.getMessage("Unexpected error occurred", null, Locale.getDefault());
        MyBoardWorkFlow baseResponse = new MyBoardWorkFlow();
        baseResponse.addError(internalError);
        return new MyBoardResponse(baseResponse);
    }
}
