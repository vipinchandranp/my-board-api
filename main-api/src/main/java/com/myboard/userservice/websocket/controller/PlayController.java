package com.myboard.userservice.websocket.controller;

import com.myboard.userservice.websocket.models.WebSocketRequest;
import com.myboard.userservice.websocket.models.WebSocketResponse;
import com.myboard.userservice.websocket.service.PlayService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller // Use @Controller instead of @RestController
public class PlayController {

    private final PlayService playService;

    public PlayController(PlayService playService) {
        this.playService = playService;
    }

    // Handle incoming WebSocket messages for "register_display"
    @MessageMapping("/register_display")
    @SendTo("/topic/register")
    public WebSocketResponse registerDisplay(WebSocketRequest request) {
        System.out.println("Received WebSocketRequest: " + request);
        String displayPin = (String) request.getData().get("displayPin");
        System.out.println("Extracted displayPin: " + displayPin);

        boolean success = playService.registerDisplay(displayPin);
        return new WebSocketResponse(
                request.getAction(),
                success ? "Display registered successfully" : "Display registration failed"
        );
    }

    // Handle incoming WebSocket messages for "play_content"
    @MessageMapping("/play_content")
    @SendTo("/topic/play")
    public WebSocketResponse playContent(WebSocketRequest request) {
        String displayPin = (String) request.getData().get("displayPin");
        String boardId = playService.playContent(displayPin);

        return new WebSocketResponse(
                request.getAction(),
                boardId
        );
    }

    // Handle incoming WebSocket messages for "stop_content"
    @MessageMapping("/stop_content")
    @SendTo("/topic/stop")
    public WebSocketResponse stopContent(WebSocketRequest request) {
        String displayPin = (String) request.getData().get("displayPin");
        boolean success = playService.stopContent(displayPin);

        return new WebSocketResponse(
                request.getAction(),
                success ? "Content stopped successfully" : "Failed to stop content"
        );
    }
}
