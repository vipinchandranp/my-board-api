package com.myboard.userservice.controller.model.display.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myboard.userservice.controller.model.common.MediaFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor // Add no-args constructor if required
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
    private double price;

    // Add a list of board IDs associated with the display
    private List<String> boardIds; // Changed to List<String> for board IDs
    private String displayPin;
    // Explicit constructor (if you want to manually define it)
    public DisplayGetDisplaysResponse(
            String displayId,
            String displayName,
            List<MediaFile> mediaFiles,
            LocalDateTime createdDateAndTime,
            String status,
            double latitude,
            double longitude,
            List<String> boardIds,
            String displayPin,
            double price) {
        this.displayId = displayId;
        this.displayName = displayName;
        this.mediaFiles = mediaFiles;
        this.createdDateAndTime = createdDateAndTime;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.boardIds = boardIds;
        this.displayPin = displayPin;
        this.price = price;
    }
}
