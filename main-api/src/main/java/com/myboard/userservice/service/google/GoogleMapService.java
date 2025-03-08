package com.myboard.userservice.service.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myboard.userservice.controller.model.google.CitiesResponse;
import com.myboard.userservice.exception.MBException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleMapService {

    @Value("${myboard.google.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<CitiesResponse> getCities(String query) throws MBException {
        // Validate query
        if (query == null || query.trim().isEmpty()) {
            throw new MBException("Query cannot be empty");
        }

        try {
            // Call the Google Places API with location restriction for India
            String url = String.format(
                    "https://maps.googleapis.com/maps/api/place/textsearch/json?query=%s+in+India&key=%s",
                    query,
                    apiKey
            );

            String response = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response);

            // Parse the response
            List<CitiesResponse> cities = new ArrayList<>();
            if (rootNode.has("results")) {
                for (JsonNode resultNode : rootNode.get("results")) {
                    String name = resultNode.get("name").asText();
                    double lat = resultNode.get("geometry").get("location").get("lat").asDouble();
                    double lng = resultNode.get("geometry").get("location").get("lng").asDouble();
                    cities.add(new CitiesResponse(name, lat, lng));
                }
            }
            return cities;

        } catch (Exception e) {
            throw new MBException("Error fetching cities from Google API", e);
        }
    }
}
