package com.myboard.userservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DateTimeSlot {
	private LocalDate date;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime startTime;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime endTime;

	private String display;

	public DateTimeSlot() {
		// Default constructor required for deserialization
	}

	public DateTimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime, String display) {
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.display = display;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
}
