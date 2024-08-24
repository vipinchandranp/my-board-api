package com.myboard.userservice.controller.model.board;

import com.myboard.userservice.controller.model.common.MainRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper = true)
public class BoardSaveRequest extends MainRequest {
    private String boardId;
    private String name;
    private MultipartFile mediaContent;
}
