package com.myboard.userservice.controller.model.timeslot.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeslotStatusByDateResponse {

    private LocalDateTime date; // The date of the timeslot
    private String boardId;     // The ID of the associated board
    private String displayId;   // The ID of the associated display
    private String status;      // The status of the timeslot (could be ACTIVE, WAITING_FOR_APPROVAL, etc.)
}
