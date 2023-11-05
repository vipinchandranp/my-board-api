package com.myboard.userservice.entity;

import java.util.List;

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

	private List<Comment> comments;

	private List<DateTimeSlot> displayDetails;

	public Board() {
	}

	public Board(String title, String description, String userId, List<Comment> comments,
			List<DateTimeSlot> displayDetails) {
		this.title = title;
		this.description = description;
		this.userId = userId;
		this.comments = comments;
		this.displayDetails = displayDetails;
	}

	public String getId() {
		return id;
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

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<DateTimeSlot> getDisplayDetails() {
		return displayDetails;
	}

	public void setDisplayDetails(List<DateTimeSlot> displayDetails) {
		this.displayDetails = displayDetails;
	}
}