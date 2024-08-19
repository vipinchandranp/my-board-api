package com.myboard.userservice.controller.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginRequest extends MainRequest {

    private String username;

    private String password;

    private String email;

    private Integer phone;

}
