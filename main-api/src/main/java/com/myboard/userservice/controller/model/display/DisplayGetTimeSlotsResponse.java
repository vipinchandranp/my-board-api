package com.myboard.userservice.controller.model.display;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myboard.userservice.controller.model.common.MainRequest;
import com.myboard.userservice.entity.TimeSlot;
import com.myboard.userservice.util.LocalDateDeserializer;
import com.myboard.userservice.util.LocalDateSerializer;
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
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    private List<TimeSlot> timeSlots;
}
