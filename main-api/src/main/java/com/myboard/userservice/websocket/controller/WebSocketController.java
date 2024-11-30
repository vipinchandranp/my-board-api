package com.myboard.userservice.websocket.controller;

import com.myboard.userservice.service.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final NotificationService notificationService;

    public WebSocketController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @MessageMapping("/notifications")
    public void handleMarkAsReadMessage(String notificationId) {
        notificationService.handleIncomingMessage(notificationId);
    }

}
