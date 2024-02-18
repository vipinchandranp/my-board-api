package com.myboard.userservice.entity;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotAvailability {

	private List<String> availableTimeSlots;
	private List<String> bookedTimeSlots;

	public TimeSlotAvailability() {
		this.availableTimeSlots = new ArrayList<>();
		this.bookedTimeSlots = new ArrayList<>();
	}

	public List<String> getAvailableTimeSlots() {
		return availableTimeSlots;
	}

	public void setAvailableTimeSlots(List<String> availableTimeSlots) {
		this.availableTimeSlots = availableTimeSlots;
	}

	public List<String> getBookedTimeSlots() {
		return bookedTimeSlots;
	}

	public void setBookedTimeSlots(List<String> bookedTimeSlots) {
		this.bookedTimeSlots = bookedTimeSlots;
	}

	public void addBookedTimeSlot(String bookedTimeSlot) {
		this.bookedTimeSlots.add(bookedTimeSlot);
	}
}
