package com.myboard.userservice.notification.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myboard.userservice.types.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data // Generates getters, setters, toString, equals, and hashcode methods
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
public class NotificationDTO {
    private String notificationId; // Nullable ID
    private NotificationType notificationType; // Type of notification
    private boolean isRead; // Indicates if the notification has been read
    private String payload; // Additional data for the notification (optional)
}
