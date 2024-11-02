package com.myboard.userservice.controller.model.timeslot.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myboard.userservice.types.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // Change this to LocalDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeslotStatusRequest {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") // Update pattern to match the incoming JSON format
    private LocalDateTime date;            // Change to LocalDateTime
    private String displayId;
    private String displayName;
    private String boardId;
    private String boardName;
    private StatusType status;
}
