package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.google.CitiesResponse;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.google.GoogleMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/map")
public class GoogleController extends BaseController {

    @Autowired
    private GoogleMapService googleService;

    /**
     * Fetches cities based on the query provided by the user.
     *
     * @param query the search query for locations
     * @return MainResponse containing a list of cities
     * @throws MBException if an error occurs while fetching cities
     */
    @GetMapping("/cities")
    public MainResponse<List<CitiesResponse>> getCities(@RequestParam(value = "query", required = false, defaultValue = "city") String query)
            throws MBException {
        List<CitiesResponse> cities = googleService.getCities(query);
        return buildResponse(cities);
    }
}
