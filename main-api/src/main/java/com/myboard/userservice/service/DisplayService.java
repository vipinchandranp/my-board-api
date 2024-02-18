package com.myboard.userservice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.myboard.userservice.dto.SelectLocationDTO;
import com.myboard.userservice.dto.TimeSlotAvailabilityDTO;
import com.myboard.userservice.entity.Display;

public interface DisplayService {
	void saveDisplay(Display displayDetails, MultipartFile imageFile);

	Display getDisplay(String displayId);

	List<Display> getAllDisplays();

	void deleteDisplay(String displayId);

	List<Display> getAllDisplaysForLoggedInUser();

	byte[] getDisplayImage(String displayId);

	List<Display> getDisplaysNearby(SelectLocationDTO locationDTO, double radius);

	TimeSlotAvailabilityDTO getDisplayTimeSlots(String displayId, LocalDate date);


}
