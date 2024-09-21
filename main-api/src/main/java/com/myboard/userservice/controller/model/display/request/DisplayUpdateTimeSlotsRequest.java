package com.myboard.userservice.controller.model.display.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.myboard.userservice.controller.model.common.TimeslotRequest;
import com.myboard.userservice.util.LocalDateDeserializer;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DisplayUpdateTimeSlotsRequest {
    private String displayId;
    private String boardId;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    private List<TimeslotRequest> timeslots;
}
