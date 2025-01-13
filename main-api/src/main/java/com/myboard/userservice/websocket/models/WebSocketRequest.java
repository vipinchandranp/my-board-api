package com.myboard.userservice.websocket.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketRequest {
    private String action;
    private Map<Object, Object> data;
}
