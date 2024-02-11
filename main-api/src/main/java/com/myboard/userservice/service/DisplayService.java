package com.myboard.userservice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.myboard.userservice.dto.SelectLocationDTO;
import com.myboard.userservice.dto.TimeSlotAvailabilityDTO;
import com.myboard.userservice.entity.DisplayDetails;

public interface DisplayService {
	void saveDisplay(DisplayDetails displayDetails, MultipartFile imageFile);

	DisplayDetails getDisplay(String displayId);

	List<DisplayDetails> getAllDisplays();

	void deleteDisplay(String displayId);

	List<DisplayDetails> getAllDisplaysForLoggedInUser();

	byte[] getDisplayImage(String displayId);

	List<DisplayDetails> getDisplaysNearby(SelectLocationDTO locationDTO, double radius);

	TimeSlotAvailabilityDTO getDisplayTimeSlots(String displayId, LocalDate date);

}
