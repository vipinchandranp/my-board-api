package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.WorkFlow;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

    @Autowired
    private WorkFlow flow;

    protected <T> MainResponse<T> buildResponse(
            T data,
            long totalElements,
            int totalPages,
            int currentPage) {

        flow.setData(data);  // Set data to the flow
        flow.setTotalElements(totalElements);  // Set total elements
        flow.setTotalPages(totalPages);  // Set total pages
        flow.setCurrentPage(currentPage);  // Set current page number

        return new MainResponse<>(flow);  // Return a new MainResponse with flow
    }


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
