package com.myboard.userservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
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

}
