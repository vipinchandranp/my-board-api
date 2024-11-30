package com.myboard.userservice.websocket.models;

import lombok.Data;

import java.util.Map;

@Data
public class WebSocketRequest {
    private String action;
    private Map<String, Object> data;
}
