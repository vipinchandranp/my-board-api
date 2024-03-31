package com.myboard.userservice.controller;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.myboard.userservice.dto.DisplayDetailsDTO;
import com.myboard.userservice.dto.TimeSlotAvailabilityDTO;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.exception.DisplayNotFoundException;
import com.myboard.userservice.service.DisplayService;

import net.glxn.qrgen.QRCode;

@RestController
@RequestMapping("/v1/displays")
@CrossOrigin
public class DisplayController {

	@Autowired
	private DisplayService displayService;

	private final Environment environment;

	@Autowired
	public DisplayController(Environment environment) {
		this.environment = environment;
	}

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> saveDisplay(@RequestPart("displayName") String displayName, @RequestPart("latitude") String latitude,
			@RequestPart("longitude") String longitude, @RequestPart("imageFile") MultipartFile imageFile) {
		try {
			// Save the display details to MongoDB
			Display displayDetails = new Display();
			displayDetails.setName(displayName);
			displayDetails.setLatitude(Double.valueOf(latitude));
			displayDetails.setLongitude(Double.valueOf(longitude));

			displayService.saveDisplay(displayDetails, imageFile);

			return ResponseEntity.status(HttpStatus.OK).body("Display saved successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save display");
		}
	}

	@GetMapping("/image/{displayId}")
	public ResponseEntity<byte[]> getImage(@PathVariable String displayId) {
		try {
			byte[] imageContent = displayService.getDisplayImage(displayId);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type
			return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
		} catch (DisplayNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/image/qr")
	public ResponseEntity<byte[]> getDisplayImageQR() {
		try {
			// Generate QR code
			ByteArrayOutputStream qrOutputStream = new ByteArrayOutputStream();
			QRCode.from("65d210f4eed45c007be41b13").writeTo(qrOutputStream);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG); // Set the appropriate content type
			return new ResponseEntity<>(qrOutputStream.toByteArray(), headers, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping
	public ResponseEntity<List<DisplayDetailsDTO>> getAllDisplays() {
		List<Display> displayList = displayService.getAllDisplays();
		List<DisplayDetailsDTO> displayDTOList = mapToDTOList(displayList);
		return ResponseEntity.ok(displayDTOList);
	}

	@GetMapping("/user")
	public ResponseEntity<List<DisplayDetailsDTO>> getDisplaysForUser() {
		try {
			List<Display> userDisplayList = displayService.getAllDisplaysForLoggedInUser();
			List<DisplayDetailsDTO> userDisplayDTOList = mapToDTOList(userDisplayList);
			return ResponseEntity.ok(userDisplayDTOList);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}

	private List<DisplayDetailsDTO> mapToDTOList(List<Display> displayList) {
		List<DisplayDetailsDTO> displayDTOList = new ArrayList<>();
		for (Display displayDetails : displayList) {
			DisplayDetailsDTO displayDTO = new DisplayDetailsDTO();

			// Copy properties from entity to DTO
			displayDTO.setId(displayDetails.getId());
			displayDTO.setLatitude(displayDetails.getLatitude());
			displayDTO.setLongitude(displayDetails.getLongitude());
			displayDTO.setDescription(displayDetails.getDescription());
			displayDTO.setName(displayDetails.getName());
			displayDTO.setFileName(displayDetails.getFileName());

			// Check if the user is not null before accessing its properties
			User user = displayDetails.getUserDisplayOwner();
			if (user != null) {
				displayDTO.setUserName(user.getUsername());
			}

			// Add the DTO to the list
			displayDTOList.add(displayDTO);
		}
		return displayDTOList;
	}

	@DeleteMapping("/{displayId}")
	public ResponseEntity<String> deleteDisplay(@PathVariable String displayId) {
		try {
			displayService.deleteDisplay(displayId);
			return ResponseEntity.status(HttpStatus.OK).body("Display deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete display");
		}
	}

	@GetMapping("/nearby")
	public ResponseEntity<List<DisplayDetailsDTO>> getDisplaysNearby(@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude) {
		try {
			// Fetch the radius from the environment property
			Double radius = Double.parseDouble(environment.getProperty("myboard.map.radius"));

			// List<DisplayDetails> displaysNearby = displayService.getDisplaysNearby(new
			// SelectLocationDTO(latitude, longitude), radius);
			List<Display> userDisplayList = displayService.getAllDisplaysForLoggedInUser();
			List<DisplayDetailsDTO> displayDTOList = mapToDTOList(userDisplayList);
			return ResponseEntity.ok(displayDTOList);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}

	@GetMapping("/timeslots")
	public ResponseEntity<TimeSlotAvailabilityDTO> getDisplayTimeSlots(@RequestParam("displayId") String displayId,
			@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS") LocalDateTime date) {
		try {
			TimeSlotAvailabilityDTO timeSlotAvailabilityDTO = displayService.getDisplayTimeSlots(displayId,
					date.toLocalDate());
			return ResponseEntity.ok(timeSlotAvailabilityDTO);
		} catch (DisplayNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new TimeSlotAvailabilityDTO(date.toLocalDate(), Collections.emptyList(), Collections.emptyList()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new TimeSlotAvailabilityDTO(date.toLocalDate(), Collections.emptyList(), Collections.emptyList()));
		}
	}

}
