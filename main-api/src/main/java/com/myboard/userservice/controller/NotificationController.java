package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.entity.Notification;
import com.myboard.userservice.notification.model.NotificationDTO;
import com.myboard.userservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/notification")
public class NotificationController extends BaseController {

    @Autowired
    private NotificationService notificationService;

    @PutMapping("/mark-as-read/{notificationId}")
    public MainResponse<String> markNotificationAsRead(@PathVariable String notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return buildResponse("Notification updated successfully");
    }

    @GetMapping("/all")
    public MainResponse<Page<NotificationDTO>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<NotificationDTO> notifications = notificationService.getAllNotifications(page, size);
        return buildResponse(notifications);
    }
}
