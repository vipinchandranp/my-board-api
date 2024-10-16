package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.WorkFlow;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

    @Autowired
    private WorkFlow flow;

    // Method to build a response with data
    protected <T> MainResponse<T> buildResponse(T data) {
        flow.setData(data);
        return new MainResponse<>(flow);
    }

    // Method to build a response without data
    protected <T> MainResponse<T> buildResponse() {
        return new MainResponse<>(flow);
    }

    // Method to add an info message
    protected void addInfoMessage(String message) {
        flow.addInfo(message);
    }

    // Method to add a warning message
    protected void addWarningMessage(String message) {
        flow.addWarn(message);
    }

    // Method to add an error message
    protected void addErrorMessage(String message) {
        flow.addError(message);
    }
}
