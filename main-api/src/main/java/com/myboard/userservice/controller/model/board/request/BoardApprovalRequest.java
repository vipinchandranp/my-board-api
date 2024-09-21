package com.myboard.userservice.controller.model.board.request;

import lombok.Data;

@Data
public class BoardApprovalRequest {
    private String boardId;
    private boolean approve;
}
