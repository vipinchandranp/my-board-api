package com.myboard.userservice.websocket.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myboard.userservice.service.TimeslotService;
import com.myboard.userservice.controller.model.timeslot.response.TimeSlotBoardToBePlayed;
import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.types.MessageType;
import com.myboard.userservice.websocket.types.WebSocketAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

public class MyBoardWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private TimeslotService timeslotService;

    // Handles incoming WebSocket messages
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Parse the incoming message as a JSON object to extract the action and other data
        String payload = message.getPayload();
        Map<String, Object> requestData = parseJson(payload); // Utility method to parse JSON

        // Extract the action from the request
        String action = (String) requestData.get("action");

        // Parse the action into the WebSocketAction enum
        WebSocketAction actionType = WebSocketAction.fromString(action);

        // Handle based on the action type
        switch (actionType) {
            case PLAY_CONTENT:
                handlePlayContent(session, requestData);
                break;
            case STOP_CONTENT:
                handleStopContent(session);
                break;
            case REGISTER_DISPLAY:
                handleRegisterDisplay(session, requestData);
                break;
            default:
                handleUnknownAction(session, action);
        }
    }

    // Handle the 'play_content' action
    private void handlePlayContent(WebSocketSession session, Map<String, Object> requestData) throws IOException {
        String displayPin = (String) requestData.get("displayPin");
        TimeSlotBoardToBePlayed boardToBePlayed = timeslotService.getBoardToBePlayedForDisplay(displayPin);

        MainResponse<TimeSlotBoardToBePlayed> response;
        if (boardToBePlayed != null) {
            // Create a MainResponse instance with board data and appropriate messages
            response = createResponse(boardToBePlayed, MessageType.INFO, "Board content is ready to be played.");
        } else {
            // Create a response with an error message
            response = new MainResponse<>();
            response.addError("No active board found for display pin: " + displayPin);
        }
        session.sendMessage(new TextMessage(response.toString())); // Send the response to the client
    }

    // Handle the 'stop_content' action
    private void handleStopContent(WebSocketSession session) throws IOException {
        // Logic to stop content (you can implement custom logic for stopping the display/board content)
        MainResponse<String> response = new MainResponse<>();
        response.addInfo("Content stopped successfully.");
        session.sendMessage(new TextMessage(response.toString())); // Send the response
    }

    // Handle the 'register_display' action
    private void handleRegisterDisplay(WebSocketSession session, Map<String, Object> requestData) throws IOException {
        String displayPin = (String) requestData.get("displayPin");

        // Example logic to validate or register the display pin (custom logic can be added)
        MainResponse<String> response = new MainResponse<>("Display registered with pin: " + displayPin);
        session.sendMessage(new TextMessage(response.toString())); // Send the response
    }

    // Handle unknown actions gracefully
    private void handleUnknownAction(WebSocketSession session, String action) throws IOException {
        MainResponse<String> response = new MainResponse<>();
        response.addError("Unknown action: " + action);
        session.sendMessage(new TextMessage(response.toString())); // Send the error response
    }

    // Helper method to create the MainResponse
    private <T> MainResponse<T> createResponse(T data, MessageType messageType, String message) {
        MainResponse<T> response = new MainResponse<>(data);
        response.addInfo(message);
        return response;
    }

    // Helper method to parse JSON (you can use a library like Jackson or Gson)
    private Map<String, Object> parseJson(String payload) throws JsonProcessingException {
        // Implement a proper JSON parsing utility here, using Jackson or Gson
        // This is just a placeholder for the parsing logic
        return new ObjectMapper().readValue(payload, Map.class);
    }
}
