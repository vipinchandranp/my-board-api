package com.myboard.userservice.controller.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationResponse {
    private double latitude;
    private double longitude;
    private String cityName;
}
