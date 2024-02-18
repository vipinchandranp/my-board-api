package com.myboard.userservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.myboard.userservice.dto.SelectLocationDTO;
import com.myboard.userservice.dto.TimeSlotAvailabilityDTO;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.entity.TimeSlotAvailability;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.exception.DisplayNotFoundException;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.repository.UserRepository;
import com.myboard.userservice.security.SecurityUtils;

@Service
public class DisplayServiceImpl implements DisplayService {

	@Autowired
	private Environment environment;

	private MongoTemplate mongoTemplate;

	@Autowired
	public DisplayServiceImpl(DisplayRepository displayRepository, MongoTemplate mongoTemplate) {
		this.displayRepository = displayRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Autowired
	private DisplayRepository displayRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void saveDisplay(Display displayDetails, MultipartFile imageFile) {
		// Get the directory path from the property
		String directoryPath = environment.getProperty("myboard.display.path");

		// Add any business logic or validation before saving
		User loggedInUser = SecurityUtils.getLoggedInUser();

		// Initialize userDisplays if it is null
		List<Display> userDisplays = loggedInUser.getDisplays();
		if (userDisplays == null) {
			userDisplays = new ArrayList<>();
		}

		// Set the user for the display
		displayDetails.setUserDisplayOwner(loggedInUser);

		// Save the displayDetails to obtain the ID from MongoDB
		Display savedDisplay = displayRepository.save(displayDetails);

		// Ensure displayDetails has a valid ID
		if (savedDisplay.getId() == null) {
			throw new RuntimeException("Display ID is null. Please set a valid ID before saving.");
		}

		// Process and store the single image file
		String imageFileName = storeImageFile(directoryPath, loggedInUser.getUsername(), savedDisplay.getId(),
				imageFile);

		// Save the image file name to MongoDB
		savedDisplay.setImages(Collections.singletonList(imageFileName));

		// Update the saved displayDetails with the image information
		displayRepository.save(savedDisplay);

		// Add the savedDisplay to the list of displays in the loggedInUser
		userDisplays.add(savedDisplay);
		loggedInUser.setDisplays(userDisplays);

		// Save the updated user to MongoDB
		userRepository.save(loggedInUser);
	}

	// Method to process and store a single image file
	private String storeImageFile(String directoryPath, String userName, String displayId, MultipartFile imageFile) {
		try {
			// Get the original file name from MultipartFile
			String originalFileName = imageFile.getOriginalFilename();

			// Generate a unique file name with timestamp, display ID, and original file
			// extension
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String generatedFileName = userName + "_" + displayId + "_" + UUID.randomUUID().toString() + "_" + timestamp
					+ getFileExtension(originalFileName);

			// Create the full directory path
			Path directory = Paths.get(directoryPath, userName);
			Files.createDirectories(directory);

			// Create the full file path
			Path filePath = Paths.get(directory.toString(), generatedFileName);

			// Save the file to the specified directory
			Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			return generatedFileName;
		} catch (IOException e) {
			// Handle the exception (log, throw, etc.)
			e.printStackTrace();
			throw new RuntimeException("Failed to store image file", e);
		}
	}

	@Override
	public byte[] getDisplayImage(String displayId) {
		Display displayDetails = displayRepository.findById(displayId)
				.orElseThrow(() -> new DisplayNotFoundException("Display not found with ID: " + displayId));

		// Load the image file content
		List<String> imageFileNames = displayDetails.getImages();

		if (imageFileNames != null && !imageFileNames.isEmpty()) {
			String directoryPath = environment.getProperty("myboard.display.path");
			String firstImageFileName = imageFileNames.get(0); // Get the first image file name
			return loadImageFile(directoryPath, firstImageFileName);
		}

		throw new DisplayNotFoundException("Image not found for Display ID: " + displayId);
	}

	private byte[] loadImageFile(String directoryPath, String imageFileName) {
		try {
			// Create the full file path
			User loggedInUser = SecurityUtils.getLoggedInUser();
			Path filePath = Paths.get(directoryPath + "/" + loggedInUser.getUsername(), imageFileName);

			// Read the file content into a byte array
			return Files.readAllBytes(filePath);
		} catch (IOException e) {
			// Handle the exception (log, throw, etc.)
			e.printStackTrace();
			throw new RuntimeException("Failed to load image file", e);
		}
	}

	@Override
	public List<Display> getAllDisplays() {
		Iterable<Display> displayIterable = displayRepository.findAll();
		List<Display> displayList = new ArrayList<>();
		displayIterable.forEach(displayList::add);
		return displayList;
	}

	@Override
	public void deleteDisplay(String displayId) {
		displayRepository.deleteById(displayId);
	}

	@Override
	public List<Display> getAllDisplaysForLoggedInUser() {
		try {
			// Retrieve the logged-in user
			User loggedInUser = SecurityUtils.getLoggedInUser();

			// Check if the user is not null
			if (loggedInUser != null) {
				// Retrieve displays for the logged-in user
				List<Display> userDisplays = loggedInUser.getDisplays();
				if (userDisplays != null) {
					return userDisplays;
				}
			}

			return Collections.emptyList(); // Return an empty list if no displays found
		} catch (Exception e) {
			throw new RuntimeException("Failed to get displays for logged-in user", e);
		}
	}

	// Helper method to extract file extension from a file name
	private String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
	}

	@Override
	public Display getDisplay(String displayId) {
		Display displayDetails = displayRepository.findById(displayId)
				.orElseThrow(() -> new DisplayNotFoundException("Display not found with ID: " + displayId));
		return displayDetails;
	}

	public List<Display> getDisplaysNearby(SelectLocationDTO locationDTO, double radius) {
		// Create a GeoJsonPoint from the given latitude and longitude
		GeoJsonPoint geoJsonPoint = new GeoJsonPoint(locationDTO.getLongitude(), locationDTO.getLatitude());

		// Create a circle representing the area within the specified radius
		Circle circle = new Circle(geoJsonPoint, new Distance(radius, Metrics.KILOMETERS));

		// Create a query to find displays within the specified circle
		Query query = Query.query(Criteria.where("location").withinSphere(circle));

		// Execute the query
		return mongoTemplate.find(query, Display.class);
	}

	@Override
	public TimeSlotAvailabilityDTO getDisplayTimeSlots(String displayId, LocalDate date) {
		Display displayDetails = displayRepository.findById(displayId)
				.orElseThrow(() -> new DisplayNotFoundException("Display not found with ID: " + displayId));

		// Check if the dateToTimeSlots map is not null
		Map<LocalDate, TimeSlotAvailability> dateToTimeSlots = displayDetails.getDateToTimeSlots();
		if (dateToTimeSlots != null) {
			// Retrieve time slots for the specified date
			TimeSlotAvailability timeSlotAvailability = dateToTimeSlots.getOrDefault(date, new TimeSlotAvailability());

			// Get available and booked time slots
			List<String> allTimeSlots = generateTimeSlots();
			List<String> availableTimeSlots;

			if (timeSlotAvailability.getAvailableTimeSlots().isEmpty()
					&& timeSlotAvailability.getBookedTimeSlots().isEmpty()) {
				// If both lists are empty, set available time slots to the full list of time
				// slots
				availableTimeSlots = new ArrayList<>(allTimeSlots);
			} else {
				availableTimeSlots = timeSlotAvailability.getAvailableTimeSlots();
			}

			List<String> bookedTimeSlots = timeSlotAvailability.getBookedTimeSlots();

			// Create an instance of TimeSlotAvailabilityDTO and return
			return new TimeSlotAvailabilityDTO(date, availableTimeSlots, bookedTimeSlots);
		}

		// Return an instance of TimeSlotAvailabilityDTO with both lists empty
		return new TimeSlotAvailabilityDTO(date, Collections.emptyList(), Collections.emptyList());
	}

	// Method to generate time slots based on configured time slot interval
	private List<String> generateTimeSlots() {
		// Retrieve the time slot interval from the property file
		String timeSlotInterval = environment.getProperty("myboard.display.timeSlotInterval");

		// Logic to calculate time slots based on the configured interval
		// Here, you need to implement the logic based on your requirements

		// For example, dividing the day into 15-minute slots
		List<String> timeSlots = new ArrayList<>();
		int totalMinutesInDay = 24 * 60;

		int interval = Integer.parseInt(timeSlotInterval);
		for (int minutes = 0; minutes < totalMinutesInDay; minutes += interval) {
			// Format the time slot as needed
			timeSlots.add(String.format("%02d:%02d", minutes / 60, minutes % 60));
		}

		return timeSlots;
	}

}
