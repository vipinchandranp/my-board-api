package com.myboard.userservice.controller.model.board.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.myboard.userservice.controller.model.common.MediaFile;

@Data
@AllArgsConstructor
public class BoardGetBoardsResponse {
    private String boardId;
    private String boardName;
    private List<MediaFile> mediaFiles; // Updated to use MediaFile objects
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDateAndTime;
    private String status;
}
