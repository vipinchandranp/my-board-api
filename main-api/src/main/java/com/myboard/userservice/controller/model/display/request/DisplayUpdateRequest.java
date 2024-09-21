package com.myboard.userservice.controller.model.display.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DisplayUpdateRequest {
    private String displayId;
    private MultipartFile mediaContent;
}
