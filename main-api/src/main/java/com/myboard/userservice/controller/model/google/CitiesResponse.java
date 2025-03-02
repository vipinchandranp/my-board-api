package com.myboard.userservice.controller.model.google;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CitiesResponse {
    private String cityName;
    private double latitude;
    private double longitude;
}
