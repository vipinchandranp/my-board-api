package com.myboard.userservice.websocket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlayService {

    public boolean registerDisplay(String displayPin) {
        log.info("Registering display with PIN: {}", displayPin);
        return true; // Simulate success
    }

    public String playContent(String displayPin) {
        log.info("Playing content for Display PIN: {}", displayPin);
        return "SampleBoardId1234"; // Return the boardId to confirm the content being played
    }

    public boolean stopContent(String displayPin) {
        log.info("Stopping content for Display PIN: {}", displayPin);
        return true; // Simulate success
    }
}
