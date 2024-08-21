package com.myboard.userservice.controller.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DisplaySaveTimeSlotsRequest extends MainRequest {
    private LocalDate date;
    private List<TimeslotRequest> timeslots;
}
