package com.myboard.userservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.myboard.userservice.util.LocalDateDeserializer;

public class DateTimeSlotDTO {
	private String id;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private String selectedDate; // Change type to String
	private String startTime;
	private String endTime;

	public String getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
