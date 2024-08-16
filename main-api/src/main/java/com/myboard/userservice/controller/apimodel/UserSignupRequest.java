package com.myboard.userservice.controller.apimodel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserSignupRequest extends MyBoardRequest{

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private Integer phone;

}
