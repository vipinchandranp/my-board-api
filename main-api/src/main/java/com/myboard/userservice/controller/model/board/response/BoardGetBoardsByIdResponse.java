package com.myboard.userservice.controller.model.board.response;

import com.myboard.userservice.controller.model.common.MediaFile; // Make sure to import MediaFile
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BoardGetBoardsByIdResponse {
    private String boardId;
    private String boardName;
    private LocalDateTime createdDateAndTime;
    private String status;
    private List<MediaFile> mediaFiles; // Change this to MediaFile
}
