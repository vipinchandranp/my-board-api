package com.myboard.userservice.service;

import org.springframework.stereotype.Service;

import com.myboard.userservice.dto.SelectLocationDTO;


public interface UserService {
	public void saveLocationForLoggedInUser(SelectLocationDTO selectLocationDTO);

}
