package com.myboard.userservice.entity;

import com.myboard.userservice.types.StatusType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
class Timeslot {
    private String startTime;
    private String endTime;
    private StatusType status;
}