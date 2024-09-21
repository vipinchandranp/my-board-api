package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.WorkFlow;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

    @Autowired
    private WorkFlow flow;

    protected <T> MainResponse<T> buildResponse() {
        return new MainResponse<>(flow);
    }
}
