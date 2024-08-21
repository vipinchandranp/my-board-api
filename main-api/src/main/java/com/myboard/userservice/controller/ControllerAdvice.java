package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.MainResponse;
import com.myboard.userservice.controller.model.WorkFlow;
import com.myboard.userservice.exception.MBException;
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
    private WorkFlow flow;

    @ExceptionHandler(MBException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MainResponse handleMyBoardException(MBException ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();
        System.out.println(stackTrace);
        flow.addError(stackTrace);
        return new MainResponse(flow);
    }

    @ExceptionHandler(Exception.class)
    public MainResponse handleGlobalException(Exception ex) {
        String internalError = messageSource.getMessage("Unexpected error occurred", null, Locale.getDefault());
        flow.addError(internalError);
        return new MainResponse(flow);
    }
}
