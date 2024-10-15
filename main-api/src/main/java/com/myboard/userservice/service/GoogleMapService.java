package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.googlemap.CitiesResponse;
import com.myboard.userservice.exception.MBException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleMapService {

    public List<CitiesResponse> getCities() throws MBException {
        // List of major cities along with their latitude and longitude
        List<CitiesResponse> CITIES = Arrays.asList(
                new CitiesResponse("Mumbai", 19.0760, 72.8777),
                new CitiesResponse("Delhi", 28.7041, 77.1025),
                new CitiesResponse("Bangalore", 12.9716, 77.5946),
                new CitiesResponse("Hyderabad", 17.3850, 78.4867),
                new CitiesResponse("Ahmedabad", 23.0225, 72.5714),
                new CitiesResponse("Chennai", 13.0827, 80.2707),
                new CitiesResponse("Kolkata", 22.5726, 88.3639),
                new CitiesResponse("Surat", 21.1702, 72.8311),
                new CitiesResponse("Pune", 18.5204, 73.8567),
                new CitiesResponse("Jaipur", 26.9124, 75.7873),
                new CitiesResponse("Lucknow", 26.8467, 80.9462),
                new CitiesResponse("Kanpur", 26.4499, 80.3319),
                new CitiesResponse("Nagpur", 21.1458, 79.0882),
                new CitiesResponse("Indore", 22.7196, 75.8577),
                new CitiesResponse("Thane", 19.2183, 72.9781)
        );

        // Return the list of cities with their corresponding coordinates
        return CITIES;
    }
}
