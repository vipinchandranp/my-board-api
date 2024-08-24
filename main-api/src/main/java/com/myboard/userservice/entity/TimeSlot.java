package com.myboard.userservice.entity;

import com.myboard.userservice.types.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//This for for db collections
public class TimeSlot {
    private String startTime;
    private String endTime;
    private StatusType status;
}
