package com.myboard.userservice.controller.apimodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myboard.userservice.types.MessageType;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Component
@RequestScope
public class MyBoardWorkFlow<T> {

    private T data;
    private Map<MessageType, List<String>> messages = new HashMap<>();

    public MyBoardWorkFlow() {
        // Initialize the message lists
        messages.put(MessageType.INFO, new ArrayList<>());
        messages.put(MessageType.WARNING, new ArrayList<>());
        messages.put(MessageType.ERROR, new ArrayList<>());
    }

    public void addInfo(String message) {
        messages.get(MessageType.INFO).add(message);
    }

    public void addWarn(String message) {
        messages.get(MessageType.WARNING).add(message);
    }

    public void addError(String message) {
        messages.get(MessageType.ERROR).add(message);
    }
}
