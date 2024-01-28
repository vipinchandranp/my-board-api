package com.myboard.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myboard.userservice.dto.SelectLocationDTO;
import com.myboard.userservice.entity.Location;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.UserRepository;
import com.myboard.userservice.security.SecurityUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

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

}
