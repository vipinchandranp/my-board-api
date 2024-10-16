package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.timeslot.response.TimeslotStatusResponse;
import com.myboard.userservice.service.TimeslotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timeslot")
public class TimeslotController extends BaseController {

    @Autowired
    private TimeslotService timeslotService;

    @GetMapping("/status")
    public MainResponse<List<TimeslotStatusResponse>> getTimeslotStatus() {
        List<TimeslotStatusResponse> status = timeslotService.getTimeslotsByDisplayCreator();
        return buildResponse(status);
    }

    // New endpoint to update timeslot approval status
    @PutMapping("/approval/update")
    public MainResponse<Boolean> updateTimeslotApproval(
            @RequestParam("timeslotId") String timeslotId,
            @RequestParam("isApproved") boolean isApproved) {
        boolean status = timeslotService.updateTimeslotApprovalStatus(timeslotId, isApproved);
        return buildResponse(status);
    }

}
