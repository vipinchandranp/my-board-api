package com.myboard.userservice.controller.model.display.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myboard.userservice.entity.Timeslot;
import com.myboard.userservice.util.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayGetTimeSlotsResponse {
    private String displayId;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    private List<Timeslot> timeSlots;
}
