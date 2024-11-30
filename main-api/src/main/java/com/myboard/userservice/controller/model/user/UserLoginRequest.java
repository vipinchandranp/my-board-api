package com.myboard.userservice.controller.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class UserLoginRequest {

    private String username;

    private String password;

    private String email;

    private Integer phone;

}
