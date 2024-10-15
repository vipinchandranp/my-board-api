package com.myboard.userservice.controller.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsRequest {
    private Double latitude;
    private Double longitude;
    private String cityName;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Integer phone;
    private String address;
    private String profilePicName;
}
