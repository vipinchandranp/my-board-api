package com.myboard.userservice.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "dateTimeSlots")
public class DisplayTimeSlot extends SuperEntity {

	@Id
	private String id;

	private LocalDate date;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime startTime;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime endTime;

	@DBRef
	private User displayOwnerUser;

	@DBRef
	private User boardOwnerUser;

	@DBRef
	private Board board;

	@DBRef
	private Display display;

	private Boolean approved = false;

}
