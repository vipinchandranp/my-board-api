package com.myboard.userservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SignupRequestDTO {
	private String username;
	private String password;
}
