package com.myboard.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myboard.userservice.entity.Notification;
import com.myboard.userservice.notification.model.NotificationDTO;
import com.myboard.userservice.repository.NotificationRepository;
import com.myboard.userservice.types.NotificationType;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper(); // For parsing JSON messages




    public Page<NotificationDTO> getAllNotifications(int page, int size) {
        // Set a maximum limit of 10 notifications per page if size is greater than 10
        size = Math.min(size, 10);

        // Fetch the paginated Notification entities
        Page<Notification> notificationsPage = notificationRepository.findAll(PageRequest.of(page, size));

        // Map each Notification entity to NotificationDTO
        return notificationsPage.map(notification -> new NotificationDTO(
                notification.getId(),
                notification.getNotificationType(),
                notification.isRead(),
                notification.getPayload()
        ));
    }

    public void notifyStatusChange(NotificationType notificationType, StatusType oldStatus, StatusType newStatus) {
        Notification notification = new Notification();
        notification.setNotificationType(notificationType);
        notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications() {
       return notificationRepository.findByIsReadFalse();
    }

    public void markNotificationAsRead(String notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            notification.setRead(false); // Mark as read
            notificationRepository.save(notification); // Save the updated notification
            System.out.println("Notification marked as read: " + notificationId);
        } else {
            System.out.println("Notification not found: " + notificationId);
        }
    }

    public void handleIncomingMessage(String message) {
        try {
            // Parse the incoming JSON message
            Map<String, Object> data = objectMapper.readValue(message, Map.class);
            String action = (String) data.get("action");

            // Check if the action is to mark a notification as read
            if ("markAsRead".equals(action)) {
                String notificationId = (String) data.get("notificationId");
                markNotificationAsRead(notificationId); // Mark the notification as read
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        }
    }
}
