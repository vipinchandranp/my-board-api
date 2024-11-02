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


@RestController
public class CustomErrorController implements ErrorController {

    @Autowired
    private WorkFlow flow;


    @RequestMapping("/error")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MainResponse handleError(HttpServletRequest request) {
        // Get the HTTP status code and any additional details
        Object status = request.getAttribute("javax.servlet.error.status_code");
        int statusCode = status != null ? Integer.parseInt(status.toString()) : 500;
        
        String errorMessage = "An error occurred";
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            errorMessage = "Resource not found";
        } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
            errorMessage = "Access forbidden";
        }
        flow.addError(errorMessage);
        
        return new MainResponse(flow);
    }
}
