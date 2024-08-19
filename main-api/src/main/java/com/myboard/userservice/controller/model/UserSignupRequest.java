package com.myboard.userservice.controller.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserSignupRequest extends MainRequest {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private Integer phone;

}
