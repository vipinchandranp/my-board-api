package com.myboard.userservice.controller.model.timeslot.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeSlotBoardToBePlayed {
    private String timeslotId;
    private String boardId;
    private String displayId;
    private String boardName;
    private String displayName;
    private String boardMediaPath;  // Updated field name
    private byte[] displayQrCode;
    private String message;
}
