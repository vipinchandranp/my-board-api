package com.myboard.userservice.controller.model.display.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.myboard.userservice.controller.model.common.AbstractFilterRequest;
import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.entity.Board;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class DisplayGetDisplaysRequest extends AbstractFilterRequest {
    private String displayId;
    private String displayName;
    private List<MediaFile> mediaFiles; // List of MediaFile objects

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // Format for LocalDateTime
    private LocalDateTime createdDateAndTime;

    private String status;

}
