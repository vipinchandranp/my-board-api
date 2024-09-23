package com.myboard.userservice.controller.model.display.request;

import lombok.Data;

@Data
public class DisplayGeoTagRequest {
    private String displayId;
    private double latitude;
    private double longitude;
}
