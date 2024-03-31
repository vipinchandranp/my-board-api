package com.myboard.userservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class DisplayDateTimeSlotDTO {
	private String id;
	private String name;
	private List<DateTimeSlotDTO> dateTimeSlots;
}
