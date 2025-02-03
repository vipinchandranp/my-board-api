package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.controller.model.timeslot.response.TimeSlotBoardToBePlayed;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.service.TimeslotService;
import com.myboard.userservice.types.MediaType;
import com.myboard.userservice.websocket.models.WebSocketResponse;
import com.myboard.userservice.websocket.service.PlayService;
import com.myboard.userservice.websocket.types.WebSocketAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/content")
public class PlayContentController  extends BaseController {

    @Autowired
    private PlayService playService;

    @Autowired
    private DisplayRepository displayRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TimeslotService timeslotService;

    @GetMapping("/play")
    public MainResponse<TimeSlotBoardToBePlayed> getContent(@RequestParam String displayPin) {
        // Call the service method to get the board based on the displayPin
        TimeSlotBoardToBePlayed timeSlotBoardToBePlayed = timeslotService.getBoardToBePlayedForDisplay(displayPin);
        return buildResponse(timeSlotBoardToBePlayed);
    }


}
