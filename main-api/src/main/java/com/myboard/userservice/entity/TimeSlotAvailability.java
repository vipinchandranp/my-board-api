package com.myboard.userservice.entity;

import java.util.List;

public class TimeSlotAvailability {

	private List<String> availableTimeSlots;
	private List<String> bookedTimeSlots;

	public TimeSlotAvailability(List<String> availableTimeSlots, List<String> bookedTimeSlots) {
		this.availableTimeSlots = availableTimeSlots;
		this.bookedTimeSlots = bookedTimeSlots;
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
}
