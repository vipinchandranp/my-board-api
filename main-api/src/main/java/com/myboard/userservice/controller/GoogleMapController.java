package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.googlemap.CitiesResponse;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.GoogleMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/map")
public class GoogleMapController extends BaseController {

    @Autowired
    private GoogleMapService cityService;

    @GetMapping("/cities")
    public MainResponse<List<CitiesResponse>> getCities() throws MBException {
        List<CitiesResponse> cities = cityService.getCities();
        return buildResponse(cities);
    }
}
