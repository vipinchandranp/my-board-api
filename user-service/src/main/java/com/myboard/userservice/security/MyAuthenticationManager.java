package com.myboard.userservice.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.myboard.userservice.repository.UserRepository;

@Component
public class MyAuthenticationManager implements AuthenticationManager {

	@Autowired
	private UserRepository userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		UserDetails userDetails = userDetailsService.findByUsername(username);

		if (passwordEncoder.matches(password, userDetails.getPassword())) {
			// Provide authorities (roles) associated with the user
			// For simplicity, a single role "ROLE_USER" is assigned here
			// You should replace this with the actual roles retrieved from your user
			// details
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
			return new UsernamePasswordAuthenticationToken(userDetails, password, Collections.singletonList(authority));
		} else {
			// Handle authentication failure
			// You may throw an AuthenticationException or return null
			// based on your application's requirements
			throw new org.springframework.security.core.AuthenticationException("Invalid credentials") {
			};
		}

	}
}
