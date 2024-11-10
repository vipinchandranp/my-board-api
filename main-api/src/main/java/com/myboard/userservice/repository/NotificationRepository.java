package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    // Custom method to find notifications where 'isRead' is false (unread)
    List<Notification> findByIsReadFalse();
    List<Notification> findByIsPushedFalse();
}
