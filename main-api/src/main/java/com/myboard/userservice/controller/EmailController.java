package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.email.EmailRequest;
import com.myboard.userservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private WorkFlow flow;

    @PostMapping("/send")
    public MainResponse sendEmail(@RequestBody EmailRequest emailRequest) {
        mailService.handleSendEmail(emailRequest);
        return new MainResponse<>(flow);
    }
}
