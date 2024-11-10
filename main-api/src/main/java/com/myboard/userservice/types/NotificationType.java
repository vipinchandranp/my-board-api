package com.myboard.userservice.types;

public enum NotificationType {
    APPROVAL,
    DISPLAY,
    BOARD_STATUS_CHANGED,
    USER,
    UNREAD_NOTIFICATION_COUNT;

    private final Class<? extends Enum<?>> subType;

    NotificationType() {
        this.subType = null;
    }

    NotificationType(Class<? extends Enum<?>> subType) {
        this.subType = subType;
    }

}
