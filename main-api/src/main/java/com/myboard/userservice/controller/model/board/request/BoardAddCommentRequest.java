package com.myboard.userservice.controller.model.board.request;

import lombok.Data;

@Data
public class BoardAddCommentRequest {
    private String boardId;
    private String comment;
}
