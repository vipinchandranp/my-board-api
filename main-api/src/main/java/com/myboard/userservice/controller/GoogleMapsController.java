package com.myboard.userservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.myboard.userservice.dto.SelectLocationDTO;

@RestController
@RequestMapping(value = "/google/maps")
@CrossOrigin
public class GoogleMapsController {
	private final String baseUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json";
	private final String apiKey = "AIzaSyC-K51rMP63i_87vcj5OOx7B9NkY1EKuE4";

	@GetMapping(value = "/searchLocation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SelectLocationDTO>> searchLocation(@RequestParam String query) {
		// Create a RestTemplate
		RestTemplate restTemplate = new RestTemplate();

		// Build the URL
		String url = String.format("%s?query=%s&key=%s", baseUrl, query, apiKey);

		// Make the HTTP GET request
		ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

		if (response.getStatusCodeValue() == 200) {
			// Process the response body and extract the required information
			JsonNode places = response.getBody();
			if (places != null && places.has("results") && places.get("results").isArray()
					&& places.get("results").size() > 0) {
				List<SelectLocationDTO> resultDTOList = new ArrayList<>();

				for (JsonNode result : places.get("results")) {
					String name = result.has("name") ? result.get("name").asText() : "";
					double latitude = result.has("geometry") && result.get("geometry").has("location")
							? result.get("geometry").get("location").get("lat").asDouble()
							: 0;
					double longitude = result.has("geometry") && result.get("geometry").has("location")
							? result.get("geometry").get("location").get("lng").asDouble()
							: 0;
					String formattedAddress = result.has("formatted_address") ? result.get("formatted_address").asText()
							: name; // Use name if formatted address is not available
					// Create a DTO object
					SelectLocationDTO selectLocationDTO = new SelectLocationDTO();
					selectLocationDTO.setName(formattedAddress);
					selectLocationDTO.setLongitude(longitude);
					selectLocationDTO.setLatitude(latitude);
					resultDTOList.add(selectLocationDTO);
				}

				System.out.println("Search Results: " + resultDTOList);
				return ResponseEntity.ok(resultDTOList);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			System.out.println("Error: " + response.getStatusCodeValue());
			return ResponseEntity.status(response.getStatusCode()).build();
		}
	}
}
