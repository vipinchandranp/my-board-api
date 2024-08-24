package com.myboard.userservice.controller.model.display;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myboard.userservice.controller.model.common.MainRequest;
import com.myboard.userservice.controller.model.common.TimeslotRequest;
import com.myboard.userservice.util.LocalDateDeserializer;
import com.myboard.userservice.util.LocalDateSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DisplayUpdateTimeSlotsRequest extends MainRequest {
    private String displayId;
    private String boardId;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    private List<TimeslotRequest> timeslots;
}
