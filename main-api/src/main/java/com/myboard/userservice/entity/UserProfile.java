package com.myboard.userservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class UserProfile {

    private Location selectedLocation;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String profilePicName;

}
