package com.myboard.userservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import com.myboard.userservice.entity.DisplayDetails;
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
	public void saveDisplay(DisplayDetails displayDetails, MultipartFile imageFile) {
		// Get the directory path from the property
		String directoryPath = environment.getProperty("myboard.display.path");

		// Add any business logic or validation before saving
		User loggedInUser = SecurityUtils.getLoggedInUser();

		// Initialize userDisplays if it is null
		List<DisplayDetails> userDisplays = loggedInUser.getDisplays();
		if (userDisplays == null) {
			userDisplays = new ArrayList<>();
		}

		// Set the user for the display
		displayDetails.setUser(loggedInUser);

		// Save the displayDetails to obtain the ID from MongoDB
		DisplayDetails savedDisplay = displayRepository.save(displayDetails);

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
		DisplayDetails displayDetails = displayRepository.findById(displayId)
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
	public List<DisplayDetails> getAllDisplays() {
		Iterable<DisplayDetails> displayIterable = displayRepository.findAll();
		List<DisplayDetails> displayList = new ArrayList<>();
		displayIterable.forEach(displayList::add);
		return displayList;
	}

	@Override
	public void deleteDisplay(String displayId) {
		displayRepository.deleteById(displayId);
	}

	@Override
	public List<DisplayDetails> getAllDisplaysForLoggedInUser() {
		try {
			// Retrieve the logged-in user
			User loggedInUser = SecurityUtils.getLoggedInUser();

			// Check if the user is not null
			if (loggedInUser != null) {
				// Retrieve displays for the logged-in user
				List<DisplayDetails> userDisplays = loggedInUser.getDisplays();
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
	public DisplayDetails getDisplay(String displayId) {
		DisplayDetails displayDetails = displayRepository.findById(displayId)
				.orElseThrow(() -> new DisplayNotFoundException("Display not found with ID: " + displayId));
		return displayDetails;
	}

	public List<DisplayDetails> getDisplaysNearby(SelectLocationDTO locationDTO, double radius) {
		// Create a GeoJsonPoint from the given latitude and longitude
		GeoJsonPoint geoJsonPoint = new GeoJsonPoint(locationDTO.getLongitude(), locationDTO.getLatitude());

		// Create a circle representing the area within the specified radius
		Circle circle = new Circle(geoJsonPoint, new Distance(radius, Metrics.KILOMETERS));

		// Create a query to find displays within the specified circle
		Query query = Query.query(Criteria.where("location").withinSphere(circle));

		// Execute the query
		return mongoTemplate.find(query, DisplayDetails.class);
	}

}
