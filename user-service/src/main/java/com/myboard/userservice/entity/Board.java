package com.myboard.userservice.entity;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.myboard.userservice.dto.DateTimeSlot;

@Document(collection = "boards")
public class Board {

	@Id
	private String id;

	private String title;

	private String description;

	private String userId; // Reference to the user's ID

	public Board() {
	}

	public Board(String title, String description, String userId) {
		this.title = title;
		this.description = description;
		this.userId = userId;
	}

	// Constructors, getters, and setters

	private Map<String, DateTimeSlot> displayDetails;

	// Constructors, getters, and setters



	public String getId() {
		return id;
	}

	public Map<String, DateTimeSlot> getDisplayDetails() {
		return displayDetails;
	}

	public void setDisplayDetails(Map<String, DateTimeSlot> displayDetails) {
		this.displayDetails = displayDetails;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
