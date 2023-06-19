package com.myboard.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myboard.userservice.dto.LoginRequest;
import com.myboard.userservice.dto.LoginResponse;
import com.myboard.userservice.dto.SignupRequest;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.UserRepository;
import com.myboard.userservice.security.JwtUtil;

@RestController
@RequestMapping("v1/users/")
public class UserController {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

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

		return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		User user = userRepository.findByUsername(loginRequest.getUsername());

		if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}

		//String token = jwtUtil.generateToken(user.getUsername());

		// Create a response object containing the token and user details
		LoginResponse response = new LoginResponse("", user);

		return ResponseEntity.ok(response);
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
}
