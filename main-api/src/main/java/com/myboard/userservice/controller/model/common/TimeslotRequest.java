package com.myboard.userservice.controller.model.common;

import com.myboard.userservice.types.StatusType;
import lombok.Data;

@Data
public class TimeslotRequest {
    private String startTime;
    private String endTime;
}