package com.myboard.userservice.security;

import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MBAuthManager implements AuthenticationManager {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		// Fetch the user from the repository
		Optional<User> optionalUser = userRepository.findByUsername(username);

		if (optionalUser.isEmpty()) {
			throw new BadCredentialsException("User not found");
		}

		User user = optionalUser.get();

		if (passwordEncoder.matches(password, user.getPassword())) {
			// Use the user's roles instead of hardcoding "ROLE_USER"
			return new UsernamePasswordAuthenticationToken(
					user,
					password,
					user.getAuthorities() // Get authorities (roles) from the user
			);
		} else {
			throw new BadCredentialsException("Invalid credentials");
		}
	}
}
