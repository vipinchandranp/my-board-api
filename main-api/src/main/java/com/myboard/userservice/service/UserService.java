package com.myboard.userservice.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.myboard.userservice.dto.SelectLocationDTO;

public interface UserService {

	public void saveLocationForLoggedInUser(SelectLocationDTO selectLocationDTO);

	public void saveUserProfile(String firstName, String lastName, String phoneNumber, String address,
			MultipartFile profilePicture);

	public Map<String, Object> initializeUser();
	
	public byte[] getProfilePic();
	public byte[] getProfilePic(String userId);


}
