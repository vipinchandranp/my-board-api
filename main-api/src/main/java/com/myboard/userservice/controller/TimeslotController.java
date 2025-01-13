package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.timeslot.request.TimeslotStatusRequest;
import com.myboard.userservice.controller.model.timeslot.response.TimeSlotBoardToBePlayed;
import com.myboard.userservice.controller.model.timeslot.response.TimeslotStatusResponse;
import com.myboard.userservice.service.TimeslotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/timeslot")
public class TimeslotController extends BaseController {

    @Autowired
    private TimeslotService timeslotService;

    @PostMapping("/status")
    public List<TimeslotStatusResponse> getTimeslotsByFilter(@RequestBody TimeslotStatusRequest request) {
        // Call the service method to get filtered timeslots based on the request
        return timeslotService.getTimeslotsByFilter(request);
    }

    // New endpoint to update timeslot approval status
    @PutMapping("/approval/update")
    public MainResponse<Boolean> updateTimeslotApproval(
            @RequestParam("timeslotId") String timeslotId,
            @RequestParam("isApproved") boolean isApproved) {
        boolean status = timeslotService.updateTimeslotApprovalStatus(timeslotId, isApproved);
        return buildResponse(status);
    }

    // New endpoint to get available dates for the logged-in user's displays
    @GetMapping("/available-dates")
    public ResponseEntity<List<LocalDate>> getAvailableDatesForUserDisplays() {
        List<LocalDate> availableDates = timeslotService.getAvailableDatesForUserDisplays();
        return ResponseEntity.ok(availableDates);
    }

/*    // New endpoint to get the board to be played based on display pin and current time

    // New endpoint to get the board to be played based on display pin
    @GetMapping("/play/board")
    public MainResponse<TimeSlotBoardToBePlayed> getBoardForDisplay(@RequestParam String displayPin) {
        // Call the service method to get the board based on the displayPin
        TimeSlotBoardToBePlayed timeSlotBoardToBePlayed = timeslotService.getBoardToBePlayedForDisplay(displayPin);
        return buildResponse(timeSlotBoardToBePlayed);
    }*/
}