package com.myboard.userservice.controller.model.board.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myboard.userservice.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.myboard.userservice.controller.model.common.MediaFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BoardGetBoardsResponse {
    private String boardId;
    private String boardName;
    private List<MediaFile> mediaFiles; // List of MediaFile objects

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // Format for LocalDateTime
    private LocalDateTime createdDateAndTime;

    private String status;

    // Optionally, you can add a constructor that accepts a Board entity to map from the Board entity to the response DTO
    public BoardGetBoardsResponse(Board board) {
        this.boardId = board.getId(); // Assuming Board entity has a method getId()
        this.boardName = board.getName(); // Assuming Board entity has a method getName()
        this.mediaFiles = board.getMediaFiles(); // Assuming Board entity has a method getMediaFiles()
        this.createdDateAndTime = board.getCreatedTime(); // Assuming Board entity has a method getCreatedAt() returning LocalDateTime
        this.status = board.getStatus().getValue(); // Assuming Board entity has a method getStatus()
    }
}
