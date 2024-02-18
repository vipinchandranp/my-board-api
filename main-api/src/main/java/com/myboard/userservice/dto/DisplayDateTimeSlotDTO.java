package com.myboard.userservice.dto;

import java.util.List;

public class DisplayDateTimeSlotDTO {
	private String id;
	private String name;
	private List<DateTimeSlotDTO> dateTimeSlots;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DateTimeSlotDTO> getDateTimeSlots() {
		return dateTimeSlots;
	}

	public void setDateTimeSlots(List<DateTimeSlotDTO> dateTimeSlots) {
		this.dateTimeSlots = dateTimeSlots;
	}

}
