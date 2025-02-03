package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.WorkFlow;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestController
public class CustomErrorController implements ErrorController {

    @Autowired
    private WorkFlow flow;
/*
    @RequestMapping("/myboard/error")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MainResponse handleError(HttpServletRequest request) {
        // Extract status code and exception details
        Object status = request.getAttribute("javax.servlet.error.status_code");
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        int statusCode = status != null ? Integer.parseInt(status.toString()) : 500;

        String errorMessage = "An error occurred";
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            errorMessage = "Resource not found";
        } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
            errorMessage = "Access forbidden";
        }

        // Get stack trace as a string
        String stackTrace = "";
        if (throwable != null) {
            StringWriter stringWriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringWriter));
            stackTrace = stringWriter.toString();
        }

        // Log error and stack trace (optional)
        System.err.println("Error: " + errorMessage);
        System.err.println("StackTrace: " + stackTrace);

        // Add error and stack trace to the workflow
        flow.addError(errorMessage);
        if (!stackTrace.isEmpty()) {
            flow.addError(stackTrace);
        }

        // Return response
        return new MainResponse(flow);
    }

    @RequestMapping("/error")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MainResponse error(HttpServletRequest request) {
        return handleError(request); // Delegate to handleError for consistency
    }*/
}
