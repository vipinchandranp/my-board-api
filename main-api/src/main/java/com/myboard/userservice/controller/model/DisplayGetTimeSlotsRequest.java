package com.myboard.userservice.controller.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class DisplayGetTimeSlotsRequest extends MainRequest {
    private LocalDate date;
}
