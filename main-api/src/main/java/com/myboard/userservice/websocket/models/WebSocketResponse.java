package com.myboard.userservice.websocket.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketResponse<T> {
    private String action;
    private T data; // Data can be any type or collection
}
