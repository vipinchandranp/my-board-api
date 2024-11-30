package com.myboard.userservice.websocket.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebSocketResponse {
    private String action;
    private String message;
}
