package com.myboard.userservice.dto;

import java.time.LocalDate;
import java.util.List;

public class TimeSlotAvailabilityDTO {
	private LocalDate selectedDate;
	private List<String> availableTimeSlots;
	private List<String> bookedTimeslots;

	public TimeSlotAvailabilityDTO(LocalDate selectedDate, List<String> availableTimeSlots,
			List<String> unavailableTimeSlots) {
		this.selectedDate = selectedDate;
		this.availableTimeSlots = availableTimeSlots;
		this.bookedTimeslots = unavailableTimeSlots;
	}

	public LocalDate getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(LocalDate selectedDate) {
		this.selectedDate = selectedDate;
	}

	public List<String> getAvailableTimeSlots() {
		return availableTimeSlots;
	}

	public void setAvailableTimeSlots(List<String> availableTimeSlots) {
		this.availableTimeSlots = availableTimeSlots;
	}

	public List<String> getBookedTimeslots() {
		return bookedTimeslots;
	}

	public void setBookedTimeslots(List<String> bookedTimeslots) {
		this.bookedTimeslots = bookedTimeslots;
	}

}
