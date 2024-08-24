package com.myboard.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    private String startTime;
    private String endTime;
    private String status;     // e.g., "09:00-10:00"
    private boolean isAvailable;

}
