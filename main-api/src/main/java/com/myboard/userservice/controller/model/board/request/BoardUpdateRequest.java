package com.myboard.userservice.controller.model.board.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardUpdateRequest {
    private String boardId;
    private MultipartFile mediaContent;
}
