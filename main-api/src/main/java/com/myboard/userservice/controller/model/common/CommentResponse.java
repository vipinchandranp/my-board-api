package com.myboard.userservice.controller.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponse {
    private String commentId;
    private String text;
    private LocalDateTime createdTime;
    private String profilePicName;
    private String userName;
}
