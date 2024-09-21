package com.myboard.userservice.controller.model.display.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DisplayGetTimeSlotsRequest  {
    private String displayId;
    private LocalDate date;
}
