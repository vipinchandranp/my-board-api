package com.myboard.userservice.controller.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myboard.userservice.types.StatusType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeslotRequest {
    private String startTime;
    private String endTime;
}