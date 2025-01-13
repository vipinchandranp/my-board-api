package com.myboard.userservice.websocket.controller;

import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.controller.model.timeslot.response.TimeSlotBoardToBePlayed;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.service.TimeslotService;
import com.myboard.userservice.types.MediaType;
import com.myboard.userservice.websocket.models.WebSocketRequest;
import com.myboard.userservice.websocket.models.WebSocketResponse;
import com.myboard.userservice.websocket.service.PlayService;
import com.myboard.userservice.websocket.types.WebSocketAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller // Use @Controller instead of @RestController
public class PlayController {

    private final PlayService playService;

    @Autowired
    private DisplayRepository displayRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TimeslotService timeslotService;



    public PlayController(PlayService playService) {
        this.playService = playService;
    }

    @MessageMapping("/register_display")
    @SendTo("/topic/register")
    public WebSocketResponse registerDisplay(WebSocketRequest request, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Received WebSocketRequest: " + request);

        String username = principal.getName();
        String displayPin = (String) request.getData().get("displayPin");
        System.out.println("Extracted displayPin: " + displayPin);

        // Get the board or QR code for the display pin
        TimeSlotBoardToBePlayed timeSlotBoardToBePlayed = timeslotService.getBoardToBePlayedForDisplay(displayPin, username);

        if (timeSlotBoardToBePlayed == null) {
            // Generate QR Code response if no board is associated with the display
            Optional<Display> displayOpt = displayRepository.findByDisplayPin(displayPin);
            if (displayOpt.isPresent()) {
                Display display = displayOpt.get();
                byte[] qrCodeBytes = timeslotService.generateQRCode(display.getId());
                if (qrCodeBytes != null) {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("qrCode", qrCodeBytes);
                    responseData.put("message", "Scan the QR code to upload your content.");

                    return new WebSocketResponse(WebSocketAction.QR_CODE.getAction(), responseData);
                }
            }
            return new WebSocketResponse(request.getAction(), "Display or QR code generation failed.");
        }

        // Ensure the board exists
        Optional<Board> boardOptional = boardRepository.findById(timeSlotBoardToBePlayed.getBoardId());
        if (boardOptional.isEmpty()) {
            return new WebSocketResponse(request.getAction(), "Board not found for displayPin: " + displayPin);
        }

        Board board = boardOptional.get();

        // Ensure the board has media files
        if (board.getMediaFiles() == null || board.getMediaFiles().isEmpty()) {
            return new WebSocketResponse(request.getAction(), "No media files found for board associated with displayPin: " + displayPin);
        }

        // Get the first media file from the board
        MediaFile media = board.getMediaFiles().get(0);

        // Attempt to register the display
        boolean success = playService.registerDisplay(displayPin);

        // Construct the response message
        String responseMessage = success
                ? "Display registered successfully. Media file: " + media.getFileName()
                : "Display registration failed";

        WebSocketResponse<Map> webSocketResponse = new WebSocketResponse<>();
        Map<MediaType, String> content = new HashMap<>();
        content.put(MediaType.IMAGE, media.getFileName());
        webSocketResponse.setAction(WebSocketAction.PLAY_CONTENT.getAction());
        webSocketResponse.setData(content);

        return new WebSocketResponse(request.getAction(), responseMessage);
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
