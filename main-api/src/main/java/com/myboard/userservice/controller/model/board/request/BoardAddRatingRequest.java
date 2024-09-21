package com.myboard.userservice.controller.model.board.request;

import lombok.Data;

@Data
public class BoardAddRatingRequest {
    private String boardId;
    private Integer rating;
}
