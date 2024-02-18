package com.myboard.userservice.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.myboard.userservice.dto.SelectLocationDTO;
import com.myboard.userservice.dto.SignupRequest;
import com.myboard.userservice.dto.UserProfileDTO;
import com.myboard.userservice.entity.Location;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.entity.UserProfile;
import com.myboard.userservice.repository.UserRepository;
import com.myboard.userservice.security.SecurityUtils;
import com.myboard.userservice.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("v1/users/")
@CrossOrigin
public class UserController {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	public UserController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {

		if (userRepository.findByUsername(signupRequest.getUsername()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
		}

		User user = new User();
		user.setUsername(signupRequest.getUsername());
		String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
		user.setPassword(encodedPassword);

		userRepository.save(user);

		return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
	}

	@GetMapping("/login")
	public ResponseEntity<?> login(HttpServletResponse response) {
		String token = response.getHeader("Authorization");
		return ResponseEntity.ok(token);
	}

	@GetMapping("/init-user")
	public ResponseEntity<Map<String, Object>> initUser() {
		// If location is null or empty, default to India's location
		User loggedInUser = SecurityUtils.getLoggedInUser();
		Location selectedLocation = (loggedInUser.getLocation() == null) ? getDefaultLocation()
				: loggedInUser.getLocation();

		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("username", loggedInUser.getUsername());
		responseMap.put("selectedLocation", selectedLocation);

		return ResponseEntity.ok(responseMap);
	}

	private Location getDefaultLocation() {
		// Create and return a default location for India
		Location defaultLocation = new Location("India", 28.6139, 77.2090);
		// You can set other properties as needed
		return defaultLocation;
	}

	@GetMapping("/hello")
	public ResponseEntity<?> hello() {
		// This is where you'll call the UserService to handle signups
		// userService.signup(userSignupDTO);
		// Then you return the appropriate response
		// return new ResponseEntity<>(userService.signup(userSignupDTO),
		// HttpStatus.CREATED);
		// Currently just returning a placeholder response
		return ResponseEntity.ok("Testing");
	}

	@GetMapping("/getusers")
	public ResponseEntity<?> getusers() {
		List<User> allUsers = (List<User>) userRepository.findAll(); // Replace 'UserRepository' with your actual
																		// repository class
		List<String> usernames = allUsers.stream().map(User::getUsername).collect(Collectors.toList());
		return ResponseEntity.ok(usernames);
	}

	@GetMapping("{pattern}")
	public List<String> getAvailableUsers(@PathVariable String pattern) {
		try {
			if (pattern == null) {
				throw new Exception("No authenticated user found");
			}
			List<User> userList = userRepository.findByUsernameContainingIgnoreCase(pattern);
			if (userList.isEmpty()) {
				return Collections.emptyList(); // Return an empty list if no matching usernames are found
			}
			return userList.stream().map(User::getUsername).collect(Collectors.toList());
		} catch (Exception e) {
			// Handle exception
			e.printStackTrace();
			throw new RuntimeException("Failed to fetch users");
		}
	}

	@PostMapping("/save-location")
	public ResponseEntity<?> savePrimaryLocation(@RequestBody @Valid SelectLocationDTO selectLocationDTO) {
		userService.saveLocationForLoggedInUser(selectLocationDTO);
		String responseMessage = "Location saved successfully.";
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}

	@PostMapping(value = "/save-profile-details", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> saveProfileDetails(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("address") String address, @RequestPart("profilePicture") MultipartFile profilePicture) {
		try {
			// Process profile picture file
			// Save the profile picture file as needed

			// Process user profile details
			userService.saveUserProfile(firstName, lastName, phoneNumber, address, profilePicture);

			String responseMessage = "User profile details and profile picture saved successfully.";
			return ResponseEntity.ok(responseMessage);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to save user profile details and profile picture: " + e.getMessage());
		}
	}

	@GetMapping("/profile-pic")
	public ResponseEntity<byte[]> getProfilePic() {
		try {
			// Get the profile picture content
			byte[] profilePicContent = userService.getProfilePic();

			// Set the appropriate content type for the response
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);

			// Return the profile picture content in the response entity
			return new ResponseEntity<>(profilePicContent, headers, HttpStatus.OK);
		} catch (Exception e) {
			// Handle exceptions appropriately
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/user-profile")
	public ResponseEntity<?> getUserProfile() {
		try {
			// Get the logged-in user
			User loggedInUser = SecurityUtils.getLoggedInUser();

			// Get the user profile details
			UserProfile userProfile = loggedInUser.getUserProfile();

			// Convert UserProfile to UserProfileDTO
			UserProfileDTO userProfileDTO = convertToDTO(userProfile);

			// Return the user profile DTO in the response
			return ResponseEntity.ok(userProfileDTO);
		} catch (Exception e) {
			// Handle exceptions appropriately
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to fetch user profile details: " + e.getMessage());
		}
	}

	private UserProfileDTO convertToDTO(UserProfile userProfile) {
		UserProfileDTO userProfileDTO = new UserProfileDTO();
		userProfileDTO.setFirstName(userProfile.getFirstName());
		userProfileDTO.setLastName(userProfile.getLastName());
		userProfileDTO.setPhoneNumber(userProfile.getPhoneNumber());
		userProfileDTO.setAddress(userProfile.getAddress());
		return userProfileDTO;
	}

}
