package com.myboard.userservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myboard.userservice.entity.DisplayTimeSlot;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.DisplayTimeSlotRepository;

@Service
public class DisplayTimeSlotServiceImpl implements DisplayTimeSlotService {

	private final DisplayTimeSlotRepository displayTimeSlotRepository;

	@Autowired
	public DisplayTimeSlotServiceImpl(DisplayTimeSlotRepository displayTimeSlotRepository) {
		this.displayTimeSlotRepository = displayTimeSlotRepository;
	}

	@Override
	public List<DisplayTimeSlot> getAllDisplayTimeSlotsForUser(User user) {
		// Implement the logic to retrieve all display time slots for the given user
		// You can use the displayTimeSlotRepository or any other method to fetch the
		// data
		return displayTimeSlotRepository.findByDisplayOwnerUser(user);
	}

	@Override
	public boolean approveTimeSlot(String timeSlotId) {
		// Retrieve the display time slot by its ID
		Optional<DisplayTimeSlot> optionalTimeSlot = displayTimeSlotRepository.findById(timeSlotId);

		if (optionalTimeSlot.isPresent()) {
			DisplayTimeSlot timeSlot = optionalTimeSlot.get();
			// Set the approval status to true
			timeSlot.setApproved(true);
			// Save the updated time slot
			displayTimeSlotRepository.save(timeSlot);
			return true; // Approval successful
		} else {
			return false; // Time slot with the provided ID not found
		}
	}

	@Override
	public boolean rejectTimeSlot(String timeSlotId) {
		// Retrieve the display time slot by its ID
		Optional<DisplayTimeSlot> optionalTimeSlot = displayTimeSlotRepository.findById(timeSlotId);

		if (optionalTimeSlot.isPresent()) {
			DisplayTimeSlot timeSlot = optionalTimeSlot.get();
			// Set the approval status to true
			timeSlot.setApproved(false);
			// Save the updated time slot
			displayTimeSlotRepository.save(timeSlot);
			return true; // Approval successful
		} else {
			return false; // Time slot with the provided ID not found
		}
	}
}
