package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.*;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.DisplayService;
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
    public MainResponse save(@RequestBody DisplaySaveRequest displaySaveRequest) throws MBException {
        displayService.process(displaySaveRequest, APIType.DISPLAY_SAVE);
        return new MainResponse<>(flow);
    }

    @PostMapping("/update")
    public MainResponse update(@RequestBody DisplayUpdateRequest boardSaveRequest) throws MBException {
        displayService.process(boardSaveRequest, APIType.DISPLAY_UPDATE);
        return new MainResponse<>(flow);
    }

    @GetMapping("/delete")
    public MainResponse delete(@RequestParam Long id) throws MBException {
        DisplayDeleteRequest displayDeleteRequest = new DisplayDeleteRequest();
        displayDeleteRequest.setId(id);
        displayService.process(displayDeleteRequest, APIType.DISPLAY_DELETE);
        return new MainResponse<>(flow);
    }

    @GetMapping("/get")
    public MainResponse get(@RequestParam Long id) throws MBException {
        DisplayDeleteRequest displayDeleteRequest = new DisplayDeleteRequest();
        displayDeleteRequest.setId(id);
        displayService.process(displayDeleteRequest, APIType.DISPLAY_GET);
        return new MainResponse<>(flow);
    }
}
