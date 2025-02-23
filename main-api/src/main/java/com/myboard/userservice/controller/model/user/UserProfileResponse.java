package com.myboard.userservice.controller.model.user;

import lombok.Data;

@Data
public class UserProfileResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Long phone;
    private String address;
    private String cityName;
    private String profilePicName;
    private Double latitude;
    private Double longitude;
}
