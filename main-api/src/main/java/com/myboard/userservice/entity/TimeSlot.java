package com.myboard.userservice.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeSlot {
    private String startTime;
    private String endTime;
    private String status;     // e.g., "09:00-10:00"
    private boolean isAvailable;

    // No-arg constructor
    public TimeSlot() {
        // Default constructor needed for some serialization/deserialization frameworks
    }
}
