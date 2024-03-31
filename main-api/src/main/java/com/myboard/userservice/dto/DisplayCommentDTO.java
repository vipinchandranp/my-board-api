package com.myboard.userservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class DisplayCommentDTO {
    private Long id;
    private String text;
    private String username;
    private java.util.Date date;
    private List<BoardCommentReplyDTO> replies;
}
