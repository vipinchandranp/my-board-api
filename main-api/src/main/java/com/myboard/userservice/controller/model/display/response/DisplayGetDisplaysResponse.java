package com.myboard.userservice.controller.model.display.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myboard.userservice.controller.model.common.MediaFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DisplayGetDisplaysResponse {
    private String displayId;
    private String displayName;
    private List<MediaFile> mediaFiles;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDateAndTime;

    private String status;

    // Add geo-location fields
    private double latitude;
    private double longitude;
}
