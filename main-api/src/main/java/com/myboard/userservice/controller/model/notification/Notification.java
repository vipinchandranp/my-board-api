package com.myboard.userservice.controller.model.notification;

import com.myboard.userservice.types.NotificationType;

public class Notification {
    private NotificationType type;
    private String title;
    private String message;

    // Constructors
    public Notification() {}

    public Notification(NotificationType type, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;
    }

    // Getters and Setters
    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
