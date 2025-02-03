package com.myboard.userservice.notification.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myboard.userservice.notification.model.NotificationDTO;
import com.myboard.userservice.types.NotificationType;
import com.myboard.userservice.entity.Notification;
import com.myboard.userservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Set;

@Service
public class PushNotifications {

    @Autowired
    private NotificationRepository notificationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

    @Scheduled(fixedRate = 3000) // Poll for unread notifications every 3 seconds
    public void pollUnreadNotifications() {
        List<Notification> unreadNotifications = notificationRepository.findByIsReadFalse();
        int unreadCount = unreadNotifications.size(); // Count of unread notifications

        if (unreadCount > 0) {

        }
    }
}
