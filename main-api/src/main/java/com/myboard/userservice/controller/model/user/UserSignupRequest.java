package com.myboard.userservice.controller.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class UserSignupRequest {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private Integer phone;

}
