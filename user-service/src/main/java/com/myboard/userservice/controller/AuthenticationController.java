package com.myboard.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myboard.userservice.dto.AuthenticationRequest;
import com.myboard.userservice.dto.AuthenticationResponse;
import com.myboard.userservice.security.JwtUtil;
import com.myboard.userservice.service.MyUserDetailsService;

@RestController
public class AuthenticationController {

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		// ... authenticate the user ...

		// If authentication was successful, generate a JWT for the user...
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtUtil.generateToken(userDetails);

		// ... and send it in the response.
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}
