package com.myboard.userservice.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
}
