package com.myboard.userservice.controller.model.display.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisplayGetDisplaysIdNameLocationResponse {
    private String displayId;
    private String displayName;
    private double latitude;
    private double longitude;
}
