package com.myboard.userservice.service;

import com.myboard.userservice.config.MailProperties;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.email.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private WorkFlow flow;

    @Autowired
    private MailProperties mailProperties; // Inject MailProperties

    public void handleSendEmail(EmailRequest emailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailRequest.getTo());
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getSubject());
        message.setFrom(mailProperties.getUsername());  // Use the email from configuration

        mailSender.send(message);
        flow.addInfo("Email sent successfully to "+emailRequest.getTo());
    }
}
