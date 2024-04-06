package com.myboard.userservice.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "display")
public class Display extends SuperEntity {

	@Id
	private String id;
	private double latitude;
	private double longitude;
	private String description;
	private String name;
	private String fileName;
	private Boolean isMovingDisplay;

	@DBRef
	private List<DisplayComment> comments;

	private double rating;
	private String userId;

	private List<String> images;

	@DBRef
	private List<Route> routes;

	@DBRef
	private User userDisplayOwner;

	private Map<LocalDate, TimeSlotAvailability> dateToTimeSlots = new HashMap<>();

}
