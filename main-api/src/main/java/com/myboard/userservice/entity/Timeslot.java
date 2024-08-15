package com.myboard.userservice.entity;

import lombok.Data;

@Data
class Timeslot {
    private String startTime;
    private String endTime;
    private boolean available;
}