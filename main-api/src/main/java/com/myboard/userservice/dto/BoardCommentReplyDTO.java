package com.myboard.userservice.dto;

import lombok.Data;

@Data
public class BoardCommentReplyDTO {

    private String id;

    private String text;

    private String userId;

    private String commentId;

}
