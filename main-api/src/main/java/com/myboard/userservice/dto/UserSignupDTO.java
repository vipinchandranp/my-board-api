package com.myboard.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserSignupDTO {

    @NotEmpty(message = "Username is required")
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

}
