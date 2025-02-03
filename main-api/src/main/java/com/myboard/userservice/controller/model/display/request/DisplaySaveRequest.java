package com.myboard.userservice.controller.model.display.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class DisplaySaveRequest {
    private String displayName;
    private double price;
    private Double latitude;
    private Double longitude;
    private List<MultipartFile> files;
}
