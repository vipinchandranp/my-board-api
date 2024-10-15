package com.myboard.userservice.controller.model.googlemap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CitiesResponse {
    private String cityName;
    private double latitude;
    private double longitude;
}
