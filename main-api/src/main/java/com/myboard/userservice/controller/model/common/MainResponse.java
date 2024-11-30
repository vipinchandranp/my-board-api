package com.myboard.userservice.controller.model.common;

import com.myboard.userservice.types.MessageType;
import lombok.Data;

import java.util.ArrayList;
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

    // Constructor to initialize with data
    public MainResponse(T data) {
        this.data = data;
    }

    public MainResponse() {

    }

    // Method to add an INFO message
    public void addInfo(String message) {
        addMessage(MessageType.INFO, message);
    }

    // Method to add a WARNING message
    public void addWarn(String message) {
        addMessage(MessageType.WARNING, message);
    }

    // Method to add an ERROR message
    public void addError(String message) {
        addMessage(MessageType.ERROR, message);
    }

    // Helper method to add messages to the appropriate MessageType category
    private void addMessage(MessageType messageType, String message) {
        // Ensure the list of messages for the given type exists
        messages.putIfAbsent(messageType, new ArrayList<>());

        // Add the message to the corresponding list
        messages.get(messageType).add(message);
    }
}
