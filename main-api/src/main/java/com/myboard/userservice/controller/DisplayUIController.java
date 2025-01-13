package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.displayui.DisplayUiConnectRequest;
import com.myboard.userservice.controller.model.user.*;
import com.myboard.userservice.exception.MBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/displayui")
public class DisplayUIController extends BaseController {

    @Autowired
    private WorkFlow flow;

    @PostMapping("/connect")
    public MainResponse<UserLoginResponse> login(@RequestBody DisplayUiConnectRequest displayUiConnectRequest) throws MBException {

        return new MainResponse<>(flow);
    }

}
