package com.myboard.userservice.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Availability {
    private LocalDate date;
    private List<Timeslot> timeslots;
}

