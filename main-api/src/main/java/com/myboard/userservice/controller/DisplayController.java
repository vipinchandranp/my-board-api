package com.myboard.userservice.controller;

import com.myboard.userservice.controller.apimodel.*;
import com.myboard.userservice.exception.MyBoardException;
import com.myboard.userservice.service.DisplayService;
import com.myboard.userservice.service.UserService;
import com.myboard.userservice.types.APIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/board")
public class DisplayController {

    @Autowired
    private DisplayService displayService;

    @Autowired
    private WorkFlow flow;

    @PostMapping("/save")
    public MainResponse signup(@RequestBody DisplaySaveRequest displaySaveRequest) throws MyBoardException {
        displayService.process(displaySaveRequest, APIType.DISPLAY_SAVE);
        return new MainResponse<>(flow);
    }

    @PostMapping("/update")
    public MainResponse signup(@RequestBody DisplayUpdateRequest boardSaveRequest) throws MyBoardException {
        displayService.process(boardSaveRequest, APIType.DISPLAY_UPDATE);
        return new MainResponse<>(flow);
    }

    @GetMapping("/delete")
    public MainResponse signup(@RequestParam Long id) throws MyBoardException {
        DisplayDeleteRequest displayDeleteRequest = new DisplayDeleteRequest();
        displayDeleteRequest.setId(id);
        displayService.process(displayDeleteRequest, APIType.DISPLAY_DELETE);
        return new MainResponse<>(flow);
    }
}
