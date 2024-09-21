package com.myboard.userservice.controller.model.display.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DisplaySaveRequest {
    private String displayId;
    private String name;
    private double latitude;
    private double longitude;
    private MultipartFile mediaContent;
}
