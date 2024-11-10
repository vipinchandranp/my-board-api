package com.myboard.userservice.entity;

import com.myboard.userservice.types.NotificationType; // Import NotificationType
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notification")
public class Notification extends Base {

    private NotificationType notificationType; // Use NotificationType enum

    private boolean isRead = false;

    private boolean isPushed = false;

    private String payload;

}
