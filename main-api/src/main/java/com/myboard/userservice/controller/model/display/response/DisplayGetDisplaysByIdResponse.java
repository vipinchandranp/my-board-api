package com.myboard.userservice.controller.model.display.response;

import com.myboard.userservice.controller.model.common.MediaFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DisplayGetDisplaysByIdResponse {
    private String displayId;
    private String displayName;
    private LocalDateTime createdDateAndTime;
    private String status;
    private List<MediaFile> mediaFiles; // Existing field
    private double latitude; // New field for latitude
    private double longitude; // New field for longitude
}
