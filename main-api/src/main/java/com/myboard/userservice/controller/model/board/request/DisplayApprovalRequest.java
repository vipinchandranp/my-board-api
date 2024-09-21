package com.myboard.userservice.controller.model.board.request;

import lombok.Data;

@Data
public class DisplayApprovalRequest {
    private String displayId;
    private boolean approve;
}
