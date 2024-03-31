package com.myboard.userservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.myboard.userservice.util.LocalDateDeserializer;
import lombok.Data;

@Data
public class DateTimeSlotDTO {
    private String id;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private String selectedDate; // Change type to String
    private String startTime;
    private String endTime;

}
