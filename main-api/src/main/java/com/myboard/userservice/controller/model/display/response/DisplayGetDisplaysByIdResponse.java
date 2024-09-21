package com.myboard.userservice.controller.model.display.response;

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
    private List<String> mediaFiles; // Add this field
}
