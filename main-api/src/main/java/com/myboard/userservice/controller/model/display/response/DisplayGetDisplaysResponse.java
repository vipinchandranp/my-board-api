package com.myboard.userservice.controller.model.display.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myboard.userservice.controller.model.common.MediaFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisplayGetDisplaysResponse {
    private String displayId;
    private String displayName;
    private List<MediaFile> mediaFiles;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDateAndTime;

    private String status;

    // Geo-location fields
    private double latitude;
    private double longitude;
    private double price;

    // List of board IDs associated with the display
    private List<String> boardIds;
    private String displayPin;

    // Transient properties to indicate user's reaction
    private boolean likedByCurrentUser;
    private boolean dislikedByCurrentUser;

    // Fields for the number of likes and dislikes
    private int numberOfLikes;
    private int numberOfDislikes;
}
