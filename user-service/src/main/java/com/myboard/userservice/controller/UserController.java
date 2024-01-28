package com.myboard.userservice.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myboard.userservice.dto.SelectLocationDTO;
import com.myboard.userservice.dto.SignupRequest;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.Location;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.UserRepository;
import com.myboard.userservice.security.JwtUtil;
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
	UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	public UserController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
		// Validate signup request (e.g., check if username is available, password
		// requirements)

		// Check if a user with the same username already exists
		if (userRepository.findByUsername(signupRequest.getUsername()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
		}

		// Create a new user entity and populate its fields
		User user = new User();
		user.setUsername(signupRequest.getUsername());
		String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
		user.setPassword(encodedPassword);

		// Save the user in the database
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

	@GetMapping("approvalRequest/{username}")
	public Map<String, List<Board>> getApprovalRequestsForUser(String username) {
		User userDetails = userRepository.findByUsername(username);
		Map<String, List<Board>> approvalsRequiredMap = userDetails.getApprovalsRequiredMap();
		return approvalsRequiredMap;
	}

	@PostMapping("/save-location")
	public ResponseEntity<?> savePrimaryLocation(@RequestBody @Valid SelectLocationDTO selectLocationDTO) {
		userService.saveLocationForLoggedInUser(selectLocationDTO);
		String responseMessage = "Location saved successfully.";
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
}
