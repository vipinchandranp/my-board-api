package com.myboard.userservice.controller.model.displayui;

import lombok.Data;

@Data
public class DisplayUiConnectRequest {

    private String username;

    private String password;

    private String displayPin;

}
