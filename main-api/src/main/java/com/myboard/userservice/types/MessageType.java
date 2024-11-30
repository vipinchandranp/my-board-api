package com.myboard.userservice.types;

public enum MessageType {
    INFO("Information"),
    WARNING("Warning"),
    ERROR("Error");

    private final String description;

    // Constructor to initialize the description for each MessageType
    MessageType(String description) {
        this.description = description;
    }

    // Getter method for the description
    public String getDescription() {
        return description;
    }
}
