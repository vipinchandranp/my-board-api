package com.myboard.userservice.controller;

import com.myboard.userservice.controller.apimodel.MainResponse;
import com.myboard.userservice.controller.apimodel.WorkFlow;
import com.myboard.userservice.exception.MyBoardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

@RestControllerAdvice
public class ControllerAdvice {

    @Autowired
    private MessageSource messageSource;


    @Autowired
    private WorkFlow myBoardWorkFlow;

    @ExceptionHandler(MyBoardException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MainResponse handleMyBoardException(MyBoardException ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();
        System.out.println(stackTrace);
        myBoardWorkFlow.addError(stackTrace);
        return new MainResponse(myBoardWorkFlow);
    }

    @ExceptionHandler(Exception.class)
    public MainResponse handleGlobalException(Exception ex) {
        String internalError = messageSource.getMessage("Unexpected error occurred", null, Locale.getDefault());
        WorkFlow baseResponse = new WorkFlow();
        baseResponse.addError(internalError);
        return new MainResponse(baseResponse);
    }
}
