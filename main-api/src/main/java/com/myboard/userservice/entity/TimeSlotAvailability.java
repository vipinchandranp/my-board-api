package com.myboard.userservice.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class TimeSlotAvailability {

	private List<String> availableTimeSlots;
	private List<String> bookedTimeSlots;

	public TimeSlotAvailability() {
		this.availableTimeSlots = new ArrayList<>();
		this.bookedTimeSlots = new ArrayList<>();
	}

	public void addBookedTimeSlot(String bookedTimeSlot) {
		this.bookedTimeSlots.add(bookedTimeSlot);
	}
}
