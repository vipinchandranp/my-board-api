package com.myboard.userservice.controller.model.common;

import lombok.Data;

@Data
public class TimeslotRequest {

    private String startTime;
    private String endTime;
    private String status;

}