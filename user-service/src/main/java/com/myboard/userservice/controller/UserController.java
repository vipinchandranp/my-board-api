package com.myboard.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myboard.userservice.dto.UserSignupDTO;
import com.myboard.userservice.service.UserService;

@RestController
@RequestMapping("v1/users/")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody UserSignupDTO userSignupDTO) {
		// This is where you'll call the UserService to handle signups
		// userService.signup(userSignupDTO);
		// Then you return the appropriate response
		// return new ResponseEntity<>(userService.signup(userSignupDTO),
		// HttpStatus.CREATED);
		// Currently just returning a placeholder response
		return ResponseEntity.ok("Signup endpoint hit");
	}
}
