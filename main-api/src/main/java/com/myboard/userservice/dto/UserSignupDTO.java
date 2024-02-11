package com.myboard.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserSignupDTO {

  @NotEmpty(message = "Username is required")
  private String username;
  
  @Email(message = "Email should be valid")
  private String email;

  @NotEmpty(message = "Password is required")
  private String password;

  // Getters and Setters

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
