package com.myboard.userservice.controller.model.board.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myboard.userservice.controller.model.common.MediaFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class BoardGetBoardsResponse {
    private String boardId;
    private String boardName;
    private List<MediaFile> mediaFiles; // List of MediaFile objects

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // Format for LocalDateTime
    private LocalDateTime createdDateAndTime;

    private String status;

    // Transient properties indicating the user's reaction
    private boolean likedByCurrentUser;
    private boolean dislikedByCurrentUser;

    // Fields for the number of likes and dislikes
    private int numberOfLikes;
    private int numberOfDislikes;
}
