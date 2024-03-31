package com.myboard.userservice.dto;

import com.myboard.userservice.entity.User;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private User user;
}
