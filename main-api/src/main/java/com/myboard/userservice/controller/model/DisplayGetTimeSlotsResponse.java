package com.myboard.userservice.controller.model;

import com.myboard.userservice.entity.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DisplayGetTimeSlotsResponse extends MainRequest {
    private LocalDate date;
    private List<TimeSlot> timeSlots;
}
