package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.*;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.DisplayService;
import com.myboard.userservice.types.APIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public MainResponse update(@RequestBody DisplayUpdateRequest displayUpdateRequest) throws MBException {
        displayService.process(displayUpdateRequest, APIType.DISPLAY_UPDATE);
        return new MainResponse<>(flow);
    }

    @GetMapping("/delete/{id}")
    public MainResponse delete(@PathVariable Long id) throws MBException {
        DisplayDeleteRequest displayDeleteRequest = new DisplayDeleteRequest();
        displayDeleteRequest.setId(id);
        displayService.process(displayDeleteRequest, APIType.DISPLAY_DELETE);
        return new MainResponse<>(flow);
    }

    @GetMapping("/get/{id}")
    public MainResponse get(@PathVariable Long id) throws MBException {
        DisplayDeleteRequest displayDeleteRequest = new DisplayDeleteRequest();
        displayDeleteRequest.setId(id);
        displayService.process(displayDeleteRequest, APIType.DISPLAY_GET);
        return new MainResponse<>(flow);
    }

    @GetMapping("/get/time-slots")
    public MainResponse getTimeSlots(@RequestParam Long id, @RequestParam LocalDate date) throws MBException {
        DisplayGetTimeSlotsRequest displayGetTimeSlotRequest = new DisplayGetTimeSlotsRequest();
        displayGetTimeSlotRequest.setId(id);
        displayGetTimeSlotRequest.setDate(date);
        displayService.process(displayGetTimeSlotRequest, APIType.DISPLAY_GET_TIMESLOTS);
        return new MainResponse<>(flow);
    }

    @PostMapping("/save/time-slots")
    public MainResponse saveTimeSlots(@RequestBody DisplaySaveTimeSlotsRequest displaySaveTimeSlots) throws MBException {
        displayService.process(displaySaveTimeSlots, APIType.DISPLAY_SAVE_TIMESLOTS);
        return new MainResponse<>(flow);
    }
}
