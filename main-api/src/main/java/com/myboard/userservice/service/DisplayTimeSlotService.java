package com.myboard.userservice.service;

import java.util.List;

import com.myboard.userservice.entity.DisplayTimeSlot;
import com.myboard.userservice.entity.User;

public interface DisplayTimeSlotService {
	List<DisplayTimeSlot> getAllDisplayTimeSlotsForUser(User user);

	boolean approveTimeSlot(String timeSlotId);
	
	boolean rejectTimeSlot(String timeSlotId);
}
