package com.myboard.userservice.controller.apimodel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginRequest extends MyBoardRequest {

    private String username;

    private String password;

    private String email;

    private Integer phone;

}
