package com.myboard.userservice.controller.model.common;

import com.myboard.userservice.types.MessageType;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MainResponse<T> {
    private T data;
    private Map<MessageType, List<String>> messages = new HashMap<>();

    // Constructor to initialize MyBoardResponse from MyBoardWorkFlow
    public MainResponse(WorkFlow<T> myBoardWorkFlow) {
        this.data = myBoardWorkFlow.getData();  // Safely cast data to the expected type
        this.messages.putAll(myBoardWorkFlow.getMessages());
    }
}
