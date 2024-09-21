package com.myboard.userservice.controller.model.board.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardSaveRequest {
    private String boardId;
    private String name;
    private MultipartFile mediaContent;
}
