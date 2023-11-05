package com.myboard.userservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getAvailableUsers(String pattern) {
		// Implement your logic to fetch the list of users based on the pattern
		// For example, you can call the UserRepository to fetch the data
		List<User> userList = userRepository.findByUsernameContainingIgnoreCase(pattern);
		return userList;
	}
}
