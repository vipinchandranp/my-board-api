package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.timeslot.response.TimeslotStatusResponse;
import com.myboard.userservice.entity.Timeslot;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.service.MBUserDetailsService;
import com.myboard.userservice.service.TimeslotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
}
