package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.display.*;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.DisplayService;
import com.myboard.userservice.types.APIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin
@RequestMapping("/display")
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
    public MainResponse update(@RequestBody DisplayUpdateRequest displayUpdateRequest) throws MBException {
        displayService.process(displayUpdateRequest, APIType.DISPLAY_UPDATE);
        return new MainResponse<>(flow);
    }

    @GetMapping("/delete/{id}")
    public MainResponse delete(@PathVariable String displayId) throws MBException {
        DisplayDeleteRequest displayDeleteRequest = new DisplayDeleteRequest();
        displayDeleteRequest.setDisplayId(displayId);
        displayService.process(displayDeleteRequest, APIType.DISPLAY_DELETE);
        return new MainResponse<>(flow);
    }

    @GetMapping("/get/{displayId}")
    public MainResponse get(@PathVariable String displayId) throws MBException {
        DisplayGetRequest displayGetRequest = new DisplayGetRequest();
        displayGetRequest.setDisplayId(displayId);
        displayService.process(displayGetRequest, APIType.DISPLAY_GET);
        return new MainResponse<>(flow);
    }

    @GetMapping("/get/time-slots")
    public MainResponse getTimeSlots(@RequestParam String displayId, @RequestParam LocalDate date) throws MBException {
        DisplayGetTimeSlotsRequest displayGetTimeSlotRequest = new DisplayGetTimeSlotsRequest();
        displayGetTimeSlotRequest.setDisplayId(displayId);
        displayGetTimeSlotRequest.setDate(date);
        displayService.process(displayGetTimeSlotRequest, APIType.DISPLAY_GET_TIMESLOTS);
        return new MainResponse<>(flow);
    }

    @PutMapping("/update/time-slots")
    public MainResponse updateTimeSlots(@RequestBody DisplayUpdateTimeSlotsRequest displaySaveTimeSlots) throws MBException {
        displayService.process(displaySaveTimeSlots, APIType.DISPLAY_UPDATE_TIMESLOTS);
        return new MainResponse<>(flow);
    }
}
