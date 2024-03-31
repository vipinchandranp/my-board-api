package com.myboard.userservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.myboard.userservice.dto.SelectLocationDTO;
import com.myboard.userservice.entity.Location;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.entity.UserProfile;
import com.myboard.userservice.exception.ProfilePictureNotFoundException;
import com.myboard.userservice.exception.ProfilePictureReadException;
import com.myboard.userservice.repository.UserProfileRepository;
import com.myboard.userservice.repository.UserRepository;
import com.myboard.userservice.security.SecurityUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Value("${myboard.user.profile-pic-path}")
	private String profilePicPath;

	@Override
	public void saveLocationForLoggedInUser(SelectLocationDTO selectLocationDTO) {
		// Get the username of the currently logged-in user
		User loggedInUser = SecurityUtils.getLoggedInUser();

		// Create a Location entity from SelectLocationDTO
		Location location = new Location();
		location.setName(selectLocationDTO.getName());
		location.setLatitude(selectLocationDTO.getLatitude());
		location.setLongitude(selectLocationDTO.getLongitude());

		// Set the location to the user
		loggedInUser.setLocation(location);

		// Save the user with the updated location
		userRepository.save(loggedInUser);
	}

	@Override
	public void saveUserProfile(String firstName, String lastName, String phoneNumber, String address,
			MultipartFile profilePicture) {
		// Save profile picture to the specified path
		String savedFileName = saveProfilePicture(profilePicture);

		// Retrieve the logged-in user
		User loggedInUser = SecurityUtils.getLoggedInUser();

		// Create a new UserProfile entity and set its properties
		UserProfile userProfile = new UserProfile();
		userProfile.setFirstName(firstName);
		userProfile.setLastName(lastName);
		userProfile.setPhoneNumber(phoneNumber);
		userProfile.setAddress(address);
		userProfile.setProfilePicName(savedFileName);

		// Set the user ID
		userProfile.setUser(loggedInUser);

		// Save UserProfile entity
		userProfileRepository.save(userProfile);

		// Update the logged-in user's userProfile
		loggedInUser.setUserProfile(userProfile);
		userRepository.save(loggedInUser);
	}

	private String saveProfilePicture(MultipartFile profilePicture) {
		String uniqueFileName = null;
		try {
			String originalFilename = profilePicture.getOriginalFilename();
			String fileExtension = getFileExtension(originalFilename);

			// Generate a unique file name
			uniqueFileName = generateUniqueFileName() + fileExtension;

			byte[] bytes = profilePicture.getBytes();
			Path path = Paths.get(profilePicPath + "/" + uniqueFileName);
			Files.write(path, bytes);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to save profile picture");
		}
		return uniqueFileName;
	}

	// Helper method to extract file extension from a file name
	private String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
	}

	// Helper method to generate a unique file name
	private String generateUniqueFileName() {
		// Generate a unique identifier, such as a UUID
		String uniqueId = UUID.randomUUID().toString();
		return uniqueId;
	}

	@Override
	public Map<String, Object> initializeUser() {
		User loggedInUser = SecurityUtils.getLoggedInUser();
		Location selectedLocation = (loggedInUser.getLocation() == null) ? getDefaultLocation()
				: loggedInUser.getLocation();

		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("username", loggedInUser.getUsername());
		responseMap.put("selectedLocation", selectedLocation);

		return responseMap;
	}

	private Location getDefaultLocation() {
		// Create and return a default location for India
		Location defaultLocation = new Location("India", 28.6139, 77.2090);
		// You can set other properties as needed
		return defaultLocation;
	}

	@Override
	public byte[] getProfilePic() {
		// Get the logged-in user
		User loggedInUser = SecurityUtils.getLoggedInUser();

		// Retrieve the profile picture file name from the user's profile
		String profilePicFileName = null;
		UserProfile userProfile = loggedInUser.getUserProfile();
		if (userProfile != null) {
			profilePicFileName = userProfile.getProfilePicName();
		}

		// Check if the profile picture file name is available
		if (profilePicFileName == null || profilePicFileName.isEmpty()) {
			throw new ProfilePictureNotFoundException(
					"Profile picture not found for user: " + loggedInUser.getUsername());
		}

		// Construct the profile picture file path
		String profilePicFilePath = profilePicPath + "/" + profilePicFileName;

		// Check if the file exists
		Path path = Paths.get(profilePicFilePath);
		if (!Files.exists(path)) {
			throw new ProfilePictureNotFoundException(
					"Profile picture not found for user: " + loggedInUser.getUsername());
		}

		// Read the profile picture file as bytes
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			throw new ProfilePictureReadException(
					"Failed to read profile picture for user: " + loggedInUser.getUsername(), e);
		}
	}

	@Override
	public byte[] getProfilePic(String userId) {

		// Retrieve the profile picture file name from the user's profile

		User user = userRepository.findById(userId).orElseThrow();
		String profilePicFileName = null;
		UserProfile userProfile = user.getUserProfile();
		if (userProfile != null) {
			profilePicFileName = userProfile.getProfilePicName();
		}

		// Check if the profile picture file name is available
		if (profilePicFileName == null || profilePicFileName.isEmpty()) {
			throw new ProfilePictureNotFoundException(
					"Profile picture not found for user: " + user.getUsername());
		}

		// Construct the profile picture file path
		String profilePicFilePath = profilePicPath + "/" + profilePicFileName;

		// Check if the file exists
		Path path = Paths.get(profilePicFilePath);
		if (!Files.exists(path)) {
			throw new ProfilePictureNotFoundException(
					"Profile picture not found for user: " + user.getUsername());
		}

		// Read the profile picture file as bytes
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			throw new ProfilePictureReadException(
					"Failed to read profile picture for user: " + user.getUsername(), e);
		}
	}

}
