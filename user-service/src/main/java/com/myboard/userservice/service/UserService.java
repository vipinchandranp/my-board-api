package com.myboard.userservice.service;

import org.springframework.stereotype.Service;

import com.myboard.userservice.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Implement user-related business logic methods
	// ...
}
